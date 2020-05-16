package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.Controller.*;
import it.polimi.ingsw.PSP29.model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server
{
    public final static int SOCKET_PORT = 7777;
    private static GameController gc;
    private int numPlayers=0;
    private boolean timeout = false;
    private int countPlayers = 0;
    private ServerSocket socket;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * server execution
     */
    public void launch(){
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }
        gc = new GameController(this);
        launchMatch();
    }

    /**
     * start the match
     */
    public void launchMatch()
    {
        while (true) {
            System.out.println("server ready");
            System.out.println("Creating Lobby");
            while(true){
                while(countPlayers==0){
                    ClientHandler clientHandler=null;
                    clientHandler = connection(socket, clientHandler);
                    if(loginPlayer(clientHandler)){
                        if(createLobby(clientHandler)){
                            while(numPlayers != 2 && numPlayers != 3){
                                if(!write(clientHandler, "serviceMessage", "MSGE-Players number not valid\n")){
                                    break;
                                }
                                if(!createLobby(clientHandler)){
                                    break;
                                }
                            }
                            if(numPlayers==2 || numPlayers==3){
                                write(clientHandler, "serviceMessage", "MSGE-\nWait for other players\n\n");
                                clientHandlers.add(clientHandler);
                                countPlayers++;
                            }
                            else{
                                gc.getMatch().getPlayers().remove(numPlayers);
                            }
                        }
                        else{
                            gc.getMatch().getPlayers().remove(numPlayers);
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
                    if(!write(clientHandler, "serviceMessage", "MSGE-\nWait for other players\n\n")){
                        gc.getMatch().getPlayers().remove(countPlayers);
                        continue;
                    }
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }

                gc.getMatch().updatePlayers(clientHandlers);

                gc.getMatch().sortPlayers();
                sortClientHandlers();

                for(Player p : gc.getMatch().getPlayers()){
                    p.setInGame(true);
                }

                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        if(!write(clientHandler, "serviceMessage", "MSGE-You're in\n\n")){
                            gc.getMatch().getPlayer(clientHandler.getName()).setInGame(false);
                        }
                    }
                }

                System.out.println("printing board");
                gc.getMatch().inizializeBoard();
                while (gc.getMatch().getBoard() == null){ }
                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage",  "BORD-"+gc.getMatch().printBoard());
                    }
                }

                System.out.println("printing players");
                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage", "LSTP-"+gc.getMatch().printPlayers());
                    }
                }

                controlEndGame();

                System.out.println("Assigning gods");
                if(!gc.godsAssignement()){
                    controlEndGame();
                }

                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage",  "MGOD-"+gc.getMatch().getPlayer(clientHandler.getName()).getCard().getName());
                    }
                }

                System.out.println("Putting workers");
                if(!gc.putWorkers()){
                    controlEndGame();
                }


                gc.gameExe();


                newGame();
            }
        }
    }

    /**
     * control if a player disconnected from the server and start a new game
     */
    public void controlEndGame(){
        gc.getMatch().updatePlayers(clientHandlers);

        if(gc.getMatch().playersInGame()!=countPlayers){
            for(ClientHandler clientHandler : clientHandlers){
                {
                    if (clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage" , "WINM-\nYou win!!\n");
                    }
                }
            }
            newGame();
        }
    }

    /**
     * create a new game at the end of the previous one
     */
    public void newGame(){
        ArrayList<ClientHandler> newCH = new ArrayList<>();
        for(int i = 0; i<clientHandlers.size();i++){
            if(clientHandlers.get(i).getConnected()){
                newCH.add(clientHandlers.get(i));
            }else{
                if(gc.getMatch().getPlayers().size()==0){
                    try {
                        this.socket.close();
                    } catch (IOException e) {
                        System.out.println("Server closed");
                    }
                }else{
                    try{
                        gc.getMatch().getPlayers().remove(i);
                    } catch (Exception e) {
                        System.out.print("");
                    }
                }
                countPlayers--;
                clientHandlers.get(i).closeConnection();
            }
        }
        clientHandlers = newCH;
        for(int i=0; i<clientHandlers.size();i++){
            write(clientHandlers.get(i), "serviceMessage", "LIST-1) YES\n2) NO\n");
            write(clientHandlers.get(i),"interactionServer", "INDX-Would you like to play again?");
        }
        String again;
        for(int i=0; i<clientHandlers.size();i++){
                try{
                    again = read(clientHandlers.get(i));
                    if(!again.equals("1") ){
                        gc.getMatch().getPlayers().remove(i);
                        countPlayers--;
                        clientHandlers.get(i).resetConnected();
                        clientHandlers.get(i).closeConnection();
                    }
                } catch (Exception e) {
                    System.out.println("No connection");
                }
        }
        for(int i=0; i<clientHandlers.size();i++){
            write(clientHandlers.get(i),"serviceMessage", "MSGE-Waiting for players\n");
        }
        launchMatch();
    }


    /**
     *
     * wait until the method is executed
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

        if(!write(clientHandler,"interactionServer", "LOGI-Insert username: ")){
            return false;
        }
        String username = read(clientHandler);
        if(username==null){
            return false;
        }
        while(gc.getMatch().alreadyIn(username)){
            if(!write(clientHandler,"interactionServer", "LOGI-Username already in, try again: ")){
                return false;
            }
            username = read(clientHandler);
            if(username==null){
                return false;
            }
        }

        int age;

        while(true){
            try{
                if(!write(clientHandler,"interactionServer", "LOGI-Insert age: ")){
                    return false;
                }
                String str = read(clientHandler);
                if(str==null){
                    return false;
                }
                age = Integer.parseInt(str);

                break;
            } catch (NumberFormatException e){
                if(!write(clientHandler, "serviceMessage", "LOGI-Invalid input\n")){
                    return false;
                }
                continue;
            }
        }

        clientHandler.setName(username);
        clientHandler.setAge(age);
        Player player1 = new Player(username, age, countPlayers+1);
        gc.getMatch().addPlayer(player1);
        gc.getMatch().getPlayer(username).setInGame(true);
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
        processReset(clientHandler, "resetSentMessage");
        if(!clientHandler.getConnected()){
            return false;
        }
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
            while(!(boolean)method1.invoke(clientHandler) && !timeout){  }
            if(timeout){
                timeout=false;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        processReset(clientHandler, "resetReadMessage");
        String response = null;
        if(clientHandler.getConnected()){
            response = clientHandler.getMessage();
        }
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
        if(!write(clientHandler, "interactionServer", "LOBB-How many players 2 or 3? ")){
            return false;
        }
        try{
            String str = read(clientHandler);
            if(str==null){
                return false;
            }
            numPlayers = Integer.parseInt(str);
        } catch (NumberFormatException e){
            write(clientHandler, "serviceMessage", "LOBB-Invalid input\n");
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

    public void setTimeout(boolean t){
        timeout = t;
    }
}
