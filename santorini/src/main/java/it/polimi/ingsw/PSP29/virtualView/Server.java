package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.controller.GameController;
import it.polimi.ingsw.PSP29.model.Box;
import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.God;
import it.polimi.ingsw.PSP29.model.Player;

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

                while(true){ }
            }

        }
    }

    public void next(){
        myturn++;
        if(myturn==numPlayers){
            myturn=0;
        }
    }

    public void process(ClientHandler clientHandler, String meth){
        try {
            Method method1 = ClientHandler.class.getMethod(meth);
            while(!(boolean)method1.invoke(clientHandler));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void processReset(ClientHandler clientHandler, String meth){
        try {
            Method method1 = ClientHandler.class.getMethod(meth);
            method1.invoke(clientHandler);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

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

    public void write(ClientHandler clientHandler, String s, String msg){
        clientHandler.sendMessage(s, msg);
        process(clientHandler, "getSentMessage");
        processReset(clientHandler, "resetSentMessage");
    }

    public String read(ClientHandler clientHandler){
        clientHandler.takeMessage();
        process(clientHandler, "getReadMessage");
        processReset(clientHandler, "resetReadMessage");
        String response = clientHandler.getMessage();
        return response;
    }

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

    public void createLobby(ClientHandler clientHandler) {
        write(clientHandler, "interactionServer", "How many players 2 or 3? ");
        numPlayers = Integer.parseInt(read(clientHandler));
    }

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
            write(clientHandlers.get(myturn), "interactionServer", "X: ");
            int x=Integer.parseInt(read(clientHandlers.get(myturn)));
            write(clientHandlers.get(myturn), "interactionServer", "Y: ");
            int y=Integer.parseInt(read(clientHandlers.get(myturn)));
            Coordinate c = new Coordinate(x, y);
            while (!gc.controlMovement(gc.getMatch().getPlayers().get(myturn), 0, c)){
                write(clientHandlers.get(myturn), "serviceMessage", "Not valid box\n");
                write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°1\n");
                write(clientHandlers.get(myturn), "interactionServer", "X: ");
                x=Integer.parseInt(read(clientHandlers.get(myturn)));
                write(clientHandlers.get(myturn), "interactionServer", "Y: ");
                y=Integer.parseInt(read(clientHandlers.get(myturn)));
                c = new Coordinate(x, y);
            }

            write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°2\n");
            write(clientHandlers.get(myturn), "interactionServer", "X: ");
            x=Integer.parseInt(read(clientHandlers.get(myturn)));
            write(clientHandlers.get(myturn), "interactionServer", "Y: ");
            y=Integer.parseInt(read(clientHandlers.get(myturn)));
            c = new Coordinate(x, y);
            while (!gc.controlMovement(gc.getMatch().getPlayers().get(myturn), 1, c)){
                write(clientHandlers.get(myturn), "serviceMessage", "Not valid box\n");
                write(clientHandlers.get(myturn), "serviceMessage", "Insert Worker n°2\n");
                write(clientHandlers.get(myturn), "interactionServer", "X: ");
                x=Integer.parseInt(read(clientHandlers.get(myturn)));
                write(clientHandlers.get(myturn), "interactionServer", "Y: ");
                y=Integer.parseInt(read(clientHandlers.get(myturn)));
                c = new Coordinate(x, y);
            }
            i++;
        }
    }
}
