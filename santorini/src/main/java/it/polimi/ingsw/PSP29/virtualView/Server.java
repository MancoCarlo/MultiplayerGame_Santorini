package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.Controller.GameController;
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
    private int myturn = 0;
    ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * server execution
     */
    public void launch()
    {
        gc = new GameController();

        ServerSocket socket;
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

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
                    ClientHandler clientHandler=null;
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
                godsAssignement();

                System.out.println("Putting workers");
                putWorkers();

                //!!è una prova!!
                turnExe();

                while(true){ }
            }

        }
    }

    /**
     * find the index of the next player
     */
    public void next(){
        myturn++;
        if(myturn==numPlayers){
            myturn=0;
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
        Socket client = null;
        try {
            client = socket.accept();
            clientHandler = new ClientHandler(client, gc);
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

    /**
     * assign one God to each player
     */
    public void godsAssignement(){
        write(clientHandlers.get(myturn), "serviceMessage", "Choose " + numPlayers + " gods from this list");
        write(clientHandlers.get(myturn), "serviceMessage", gc.getMatch().printGodlist());
        write(clientHandlers.get(myturn), "interactionServer", "Insert n°1 index: ");
        gc.getGodIndex().add(Integer.parseInt(read(clientHandlers.get(myturn))) - 1);
        for(int i=1; i<numPlayers; i++){
            write(clientHandlers.get(myturn), "interactionServer", "Insert n°" + (i+1) + " index: ");
            gc.getGodIndex().add(Integer.parseInt(read(clientHandlers.get(myturn))) - 1);
        }
        gc.godSelection();
        int i=0;
        while (i<clientHandlers.size()){
            next();
            write(clientHandlers.get(myturn), "serviceMessage", gc.getMatch().printGodlist());
            write(clientHandlers.get(myturn), "interactionServer", "Choose one god from this list: ");
            int id = Integer.parseInt(read(clientHandlers.get(myturn))) - 1;
            gc.getMatch().getPlayers().get(myturn).setCard(gc.getMatch().getGods(), id);
            gc.getMatch().getGods().remove(id);
            i++;
        }
    }

    /**
     * ask to the client where he want to put his players
     */
    public void putWorkers(){
        int i=0;
        while (i<numPlayers){
            next();
            for(int j=0; j<clientHandlers.size(); j++){
                if(j!=myturn){
                    write(clientHandlers.get(j), "serviceMessage", "Wait your turn\n");
                }
            }
            write(clientHandlers.get(myturn), "serviceMessage",  gc.getMatch().printBoard());
            write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°1\n");
            Coordinate c = getCoordinate();
            while (!gc.controlMovement(gc.getMatch().getPlayers().get(myturn), 0, c)){
                write(clientHandlers.get(myturn), "serviceMessage", "Not valid box\n");
                write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°1\n");
                c = getCoordinate();
            }

            write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°2\n");
            c = getCoordinate();
            while (!gc.controlMovement(gc.getMatch().getPlayers().get(myturn), 1, c)){
                write(clientHandlers.get(myturn), "serviceMessage", "Not valid box\n");
                write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°2\n");
                c = getCoordinate();
            }
            i++;
        }
    }

    /**
     * !!è una prova!!
     * create an arrayList with all the coordinates in wich the worker can move
     * @param id the worker id
     * @return the list
     */
    public ArrayList<Coordinate> whereCanMove(int id){
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Player player = gc.getMatch().getPlayer(clientHandlers.get(myturn).getName());
        for(int i=0; i<gc.getMatch().getRows(); i++){
            for(int j=0; j<gc.getMatch().getColumns(); j++){
                Coordinate c = new Coordinate(i, j);
                if(player.getWorker(id).canMoveTo(c, player.getCard().getName(), gc.getMatch(), gc.getAthenaOn())){
                    coordinates.add(new Coordinate(i, j));
                }
            }
        }
        return coordinates;
    }

    /**
     * !!è una prova!!
     * the turn execution
     */
    public void turnExe(){
        next();
        int wID=2;
        ArrayList<Coordinate> coordinates0 = whereCanMove(0);
        ArrayList<Coordinate> coordinates1 = whereCanMove(1);
        if(coordinates0.size()!=0 && coordinates1.size()!=0){
            write(clientHandlers.get(myturn), "serviceMessage", "It's your turn\n");
            write(clientHandlers.get(myturn), "serviceMessage", "Choose the worker to use in this turn: \n");
            write(clientHandlers.get(myturn), "interactionServer", gc.getMatch().getPlayer(clientHandlers.get(myturn).getName()).printWorkers());
            wID = Integer.parseInt(read(clientHandlers.get(myturn)));
        }
        else if(coordinates0.size()!=0 && coordinates1.size()==0){
            write(clientHandlers.get(myturn), "serviceMessage", "You can only move one of your worker in these positions: \n");
            wID = 0;
        }
        else if(coordinates0.size()==0 && coordinates1.size()!=0){
            write(clientHandlers.get(myturn), "serviceMessage", "You can only move one of your worker in these positions: \n");
            wID = 1;
        }
        Coordinate c = null;
        if(wID==0){
            write(clientHandlers.get(myturn), "serviceMessage", printCoordinates(coordinates0));
            write(clientHandlers.get(myturn), "interactionServer", "Where you want to move?\n");
            c = coordinates0.get(Integer.parseInt(read(clientHandlers.get(myturn))));
        }
        else if(wID==1){
            write(clientHandlers.get(myturn), "serviceMessage", printCoordinates(coordinates1));
            write(clientHandlers.get(myturn), "interactionServer", "Where you want to move?\n");
            c = coordinates1.get(Integer.parseInt(read(clientHandlers.get(myturn))));
        }
        System.out.println(c);
    }

    /**
     * ask a coordinate to the client
     * @return the coordinate
     */
    public Coordinate getCoordinate(){
        Coordinate c;
        write(clientHandlers.get(myturn), "interactionServer", "X: ");
        int x=Integer.parseInt(read(clientHandlers.get(myturn)));
        write(clientHandlers.get(myturn), "interactionServer", "Y: ");
        int y=Integer.parseInt(read(clientHandlers.get(myturn)));
        c = new Coordinate(x, y);
        return c;
    }

    /**
     * !!è una prova!!
     * print the list of valids coordinate
     * @param coordinates
     * @return the string that print the list
     */
    public String printCoordinates(ArrayList<Coordinate> coordinates){
        String c = "Valid coordinates:\n";
        for(int i=0; i<coordinates.size(); i++){
            c = c + i + ") " + coordinates.get(i).toString() + "\n";
        }
        return c;
    }
}
