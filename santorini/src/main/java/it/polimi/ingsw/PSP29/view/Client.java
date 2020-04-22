package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Client implements Runnable, ServerObserver
{
    /* auxiliary variable used for implementing the consumer-producer pattern*/
    private Player player;
    private Box[][] gameboard;
    private boolean first;
    private boolean lobbyCreated;
    private String response = null;
    private boolean connection;

    public static void main( String[] args )
    {
        /* Instantiate a new Client which will also receive events from
         * the server by implementing the ServerObserver interface */
        Client client = new Client();
        client.run();
    }


    @Override
    public void run()
    {
        /*
         * WARNING: this method executes IN THE CONTEXT OF THE MAIN THREAD
         */

        Scanner scanner = new Scanner(System.in);
        response = null;
        connection = false;
        //System.out.println("IP address of server?");
        String ip = "127.0.0.8";

        /* open a connection to the server */
        Socket server;
        try {
            server = new Socket(ip, Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        /* Create the adapter that will allow communication with the server
         * in background, and start running its thread */
        ServerAdapter serverAdapter = new ServerAdapter(server);
        serverAdapter.addObserver(this);
        Thread serverAdapterThread = new Thread(serverAdapter);
        serverAdapterThread.start();
        synchronized (this) {
            while(connection == false) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("---Welcome to Santorini---\n");
            serverAdapter.readMessage();
            while(response == null) {
                try {
                    System.out.println("Wait response from server");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(response);
            response = null;
            String name = scanner.nextLine();
            int eta = Integer.parseInt(scanner.nextLine());
            lobbyCreated=false;
            player = null;
            gameboard = null;
            Player p = new Player(name, eta);
            serverAdapter.login(p);
            while (player == null) {
                try {
                    wait();
                } catch (InterruptedException e) {  }
            }
            System.out.println("\nYou've been accepted\n");
            if(first){
                serverAdapter.lobby();
                while (!lobbyCreated) {
                    try {
                        wait();
                    } catch (InterruptedException e) {  }
                }
                System.out.println("\nLobby created, wait for other players\n");
            }
            serverAdapter.printBoard();
            while (gameboard == null) {
                try {
                    wait();
                } catch (InterruptedException e) { }
            }
            outputBoard();
        }

        while(true){

        }
    }


    @Override
    public synchronized void didLogin(Player p1, Player p2, boolean f)
    {
        player = p2;
        first=f;
        notifyAll();
    }

    @Override
    public synchronized void didRead(String message)
    {
        response = message;
        notifyAll();
    }

    @Override
    public synchronized void didHandleConnection()
    {
        connection = true;
        notifyAll();
    }

    @Override
    public synchronized void didReceiveBoard(Box[][] board)
    {
        gameboard = board;
        notifyAll();
    }

    private synchronized void outputBoard(){
        System.out.println("Gameboard");
        System.out.print("  \t");
        for(int i=0; i<5; i++){
            System.out.print(i + " \t");
        }
        System.out.println();
        for(int i=0; i<5; i++){
            System.out.print(i + " \t");
            for(int j=0; j<5; j++){
                gameboard[i][j].printEmpty();
                System.out.print("\t");
            }
            System.out.println();
        }
    }


    public synchronized void didLobby(){
        lobbyCreated=true;
        notifyAll();
    }


}
