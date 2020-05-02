package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.Controller.*;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.view.Client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;


public class Server
{
    public final static int SOCKET_PORT = 7777;
    private static GameController gc;
    private int numPlayers=0;
    private boolean endGame = false;
    private boolean timeout = false;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * server execution
     */
    public void launch()
    {
        ServerSocket socket;
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }
        gc = new GameController(this);
        while (true) {
            int countPlayers = 0;
            System.out.println("server ready");
            System.out.println("Creating Lobby");
            while(true){
                while(countPlayers==0){
                    ClientHandler clientHandler=null;
                    clientHandler = connection(socket, clientHandler);
                    if(loginPlayer(clientHandler)){
                        if(createLobby(clientHandler)){
                            while(numPlayers != 2 && numPlayers != 3){
                                if(!write(clientHandler, "serviceMessage", "Players number not valid\n")){
                                    break;
                                }
                                if(!createLobby(clientHandler)){
                                    break;
                                }
                            }
                            if(numPlayers==2 || numPlayers==3){
                                write(clientHandler, "serviceMessage", "\nWait for other players\n\n");
                                clientHandlers.add(clientHandler);
                                countPlayers++;
                            }
                            else{
                                gc.getMatch().getPlayers().remove(numPlayers);
                            }
                        }
                    }
                }
                System.out.println("Adding players");
                while(countPlayers < numPlayers){
                    ClientHandler clientHandler = null;
                    clientHandler = connection(socket, clientHandler);
                    if(!loginPlayer(clientHandler)){
                        continue;
                    }
                    if(!write(clientHandler, "serviceMessage", "\nWait for other players\n\n")){
                        gc.getMatch().getPlayers().remove(countPlayers);
                        continue;
                    }
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }

                for(ClientHandler clientHandler : clientHandlers){
                    if(!write(clientHandler, "serviceMessage", "You're in\n\n")){
                        clientHandler.resetConnected();
                    }
                }

                System.out.println("printing board");
                gc.getMatch().inizializeBoard();
                while (gc.getMatch().getBoard() == null){ }
                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        if(!write(clientHandler, "serviceMessage",  gc.getMatch().printBoard())){
                            clientHandler.resetConnected();
                        }
                    }
                    else{
                        System.out.println(clientHandler.getName() + " disconnected");
                    }
                }

                System.out.println("printing players");
                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        if(!write(clientHandler, "serviceMessage", gc.getMatch().printPlayers())){
                            clientHandler.resetConnected();
                        }
                    }
                    else{
                        System.out.println(clientHandler.getName() + " disconnected");
                    }
                }

                ArrayList<ClientHandler> removed = gc.getMatch().updatePlayers(gc, clientHandlers);
                updateClientHandlers(removed);

                if(gc.getMatch().getPlayers().size()==1){
                    for(ClientHandler clientHandler : clientHandlers){
                        if (clientHandler.getName().equals(gc.getMatch().getPlayers().get(0).getNickname())){
                            write(clientHandler, "serviceMessage" , "\nYou win!!\n");
                        }
                    }
                    break;
                }

                for(Player p : gc.getMatch().getPlayers()){
                    p.setInGame(true);
                }


                //fino a qui OK con le disconnessioni
                //da qui in poi sono da sistemare nel game controller

                gc.getMatch().sortPlayers();
                sortClientHandlers();
                gc.getMatch().loadGods();

                System.out.println("Assigning gods");
                gc.godsAssignement();

                System.out.println("Putting workers");
                gc.putWorkers();


                gc.gameExe();

            }
        }
    }

    /**
     *
     * wait unthil the method is executed
     *
     * @param clientHandler
     * @param meth the method to process
     */
    public void process(ClientHandler clientHandler, String meth){
        try {
            Method method1 = ClientHandler.class.getMethod(meth);
            while(!(boolean)method1.invoke(clientHandler));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * reset the variable in clienthandler linked to the method passed
     *
     * @param clientHandler
     * @param meth the method to reset
     */
    public void processReset(ClientHandler clientHandler, String meth){
        try {
            Method method1 = ClientHandler.class.getMethod(meth);
            method1.invoke(clientHandler);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * accept a client and add him to the players list
     *
     * @param clientHandler
     */
    public boolean loginPlayer(ClientHandler clientHandler){
        if(!write(clientHandler, "serviceMessage", "Welcome to Santorini\n\n")){
            return false;
        }

        if(!write(clientHandler,"interactionServer", "Insert username: ")){
            return false;
        }
        String username = read(clientHandler);

        while(gc.getMatch().alreadyIn(username)){
            if(!write(clientHandler,"interactionServer", "Username already in, try again: ")){
                return false;
            }
            username = read(clientHandler);
        }

        int age;

        while(true){
            try{
                if(!write(clientHandler,"interactionServer", "Insert age: ")){
                    return false;
                }
                age = Integer.parseInt(read(clientHandler));
                break;
            } catch (NumberFormatException e){
                if(!write(clientHandler, "serviceMessage", "Invalid input\n")){
                    return false;
                }
                continue;
            }
        }

        clientHandler.setName(username);
        clientHandler.setAge(age);

        Player player1 = new Player(username, age);
        gc.getMatch().addPlayer(player1);
        return true;
    }

    /**
     *
     * write a message to the client
     *
     * @param clientHandler
     * @param s the type of message
     * @param msg the message
     */
    public boolean write(ClientHandler clientHandler, String s, String msg){
        clientHandler.sendMessage(s, msg);
        process(clientHandler, "getSentMessage");
        if(clientHandler.getError()){
            return false;
        }
        processReset(clientHandler, "resetSentMessage");
        return true;
    }

    /**
     *
     * read a message from the client
     *
     * @param clientHandler
     * @return the message
     */
    public String read(ClientHandler clientHandler){
        clientHandler.takeMessage();
        try {
            Method method1 = ClientHandler.class.getMethod("getReadMessage");
            while(!(boolean)method1.invoke(clientHandler) || timeout){  }
            if(timeout){

            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        processReset(clientHandler, "resetReadMessage");
        String response = clientHandler.getMessage();
        return response;
    }

    /**
     *
     * connect a client to the server
     *
     * @param socket the server
     * @param clientHandler
     * @return the clientHandler linked to the client
     */
    public ClientHandler connection(ServerSocket socket,ClientHandler clientHandler){
        Socket client;
        try {
            client = socket.accept();
            clientHandler = new ClientHandler(client, this);
            Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
            thread.start();
            process(clientHandler, "getConnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientHandler;
    }

    /**
     *
     * ask to the player how many players will be in the game
     *
     * @param clientHandler
     */
    public boolean createLobby(ClientHandler clientHandler) {
        if(!write(clientHandler, "interactionServer", "How many players 2 or 3? ")){
            return false;
        }
        try{
            numPlayers = Integer.parseInt(read(clientHandler));
        } catch (NumberFormatException e){
            write(clientHandler, "serviceMessage", "Invalid input\n");
            createLobby(clientHandler);
        }
        gc.setNumPlayers(numPlayers);
        return true;
    }

    /**
     * sort the list of clienHandlers
     */
    public void sortClientHandlers(){
        boolean change=true;
        ClientHandler ch;
        while(change){
            change=false;
            for(int i=0; i<clientHandlers.size()-1; i++){
                if(clientHandlers.get(i).getAge()>clientHandlers.get(i+1).getAge()){
                    ch=clientHandlers.get(i);
                    clientHandlers.set(i, clientHandlers.get(i+1));
                    clientHandlers.set(i+1, ch);
                    change=true;
                }
            }
        }
    }

    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void updateClientHandlers(ArrayList<ClientHandler> removed){
        for(ClientHandler clientHandler : removed){
            clientHandlers.remove(clientHandler);
        }
    }

    public void setTimeout(boolean t){
        timeout = t;
    }

    public boolean getTimeout(){
        return timeout;
    }
}
