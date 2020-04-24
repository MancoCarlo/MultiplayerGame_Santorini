package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.controller.GameController;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.view.Client;
import it.polimi.ingsw.PSP29.view.ServerAdapter;

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
            ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
            int countPlayers = 0;
            System.out.println("server ready");
            while(true){
                if(countPlayers==0){
                    ClientHandler clientHandler=null;
                    clientHandler = connection(socket, clientHandler);

                    while (numPlayers == 0){

                        loginPlayer(clientHandler);
                        createLobby(clientHandler);
                        while(numPlayers != 2 && numPlayers != 3){
                            write(clientHandler, "serviceMessage", "Players number not valid");
                            createLobby(clientHandler);
                        }
                    }
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }
                while(countPlayers < numPlayers){
                    ClientHandler clientHandler=null;
                    clientHandler = connection(socket, clientHandler);
                    loginPlayer(clientHandler);
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }
                
                for(ClientHandler clientHandler : clientHandlers){
                    write(clientHandler, "serviceMessage", "You're in");
                }
            }

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
        write(clientHandler,"interactionServer", "Insert username: ");
        String username = read(clientHandler);

        write(clientHandler,"interactionServer", "Insert age: ");
        int age = Integer.parseInt(read(clientHandler));

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
}
