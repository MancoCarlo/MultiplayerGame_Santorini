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
    private int numPlayers;
    private boolean endGame = false;
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
                if(countPlayers==0){
                    ClientHandler clientHandler=null;
                    clientHandler = connection(socket, clientHandler);

                    while (numPlayers == 0){
                        loginPlayer(clientHandler);
                        createLobby(clientHandler);
                        while(numPlayers != 2 && numPlayers != 3){
                            write(clientHandler, "serviceMessage", "Players number not valid\n");
                            createLobby(clientHandler);
                        }
                        write(clientHandler, "serviceMessage", "\nWait for other players\n\n");
                    }
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }
                System.out.println("Adding players");
                while(countPlayers < numPlayers){
                    ClientHandler clientHandler = null;
                    clientHandler = connection(socket, clientHandler);
                    loginPlayer(clientHandler);
                    write(clientHandler, "serviceMessage", "\nWait for other players\n\n");
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }

                for(ClientHandler clientHandler : clientHandlers){
                    write(clientHandler, "serviceMessage", "You're in\n\n");
                }
                System.out.println("printing board");
                gc.getMatch().inizializeBoard();
                while (gc.getMatch().getBoard() == null){ }
                for(ClientHandler clientHandler : clientHandlers){
                    write(clientHandler, "serviceMessage",  gc.getMatch().printBoard());
                }

                System.out.println("printing players");
                for(ClientHandler clientHandler : clientHandlers){
                    write(clientHandler, "serviceMessage", gc.getMatch().printPlayers());
                }

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
    public void loginPlayer(ClientHandler clientHandler){
        write(clientHandler, "serviceMessage", "Welcome to Santorini\n\n");

        write(clientHandler,"interactionServer", "Insert username: ");
        String username = read(clientHandler);

        while(gc.getMatch().alreadyIn(username)){
            write(clientHandler,"interactionServer", "Username already in, try again: ");
            username = read(clientHandler);
        }

        write(clientHandler,"interactionServer", "Insert age: ");
        int age = Integer.parseInt(read(clientHandler));

        clientHandler.setName(username);
        clientHandler.setAge(age);

        Player player1 = new Player(username, age);
        gc.getMatch().addPlayer(player1);
    }

    /**
     *
     * write a message to the client
     *
     * @param clientHandler
     * @param s the type of message
     * @param msg the message
     */
    public void write(ClientHandler clientHandler, String s, String msg){
        clientHandler.sendMessage(s, msg);
        process(clientHandler, "getSentMessage");
        processReset(clientHandler, "resetSentMessage");
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
        process(clientHandler, "getReadMessage");
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
            clientHandler = new ClientHandler(client);
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
    public void createLobby(ClientHandler clientHandler) {
        write(clientHandler, "interactionServer", "How many players 2 or 3? ");
        numPlayers = Integer.parseInt(read(clientHandler));
        gc.setNumPlayers(numPlayers);
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
}
