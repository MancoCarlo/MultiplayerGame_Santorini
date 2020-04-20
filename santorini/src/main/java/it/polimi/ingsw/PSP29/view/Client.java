package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Client implements Runnable, ServerObserver
{
    /* auxiliary variable used for implementing the consumer-producer pattern*/
    private String response = null;
    private Player player;
    private Box[][] gameboard;

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

        System.out.print("Inserisci nome e eta: ");
        String name = scanner.nextLine();
        int eta = Integer.parseInt(scanner.nextLine());
        synchronized (this) {
            response = null;
            player = null;
            gameboard = null;
            Player p = new Player(name, eta);
            serverAdapter.login(p);
            int seconds = 0;
            while (player == null) {
                try {
                    wait();
                } catch (InterruptedException e) {  }
            }
            System.out.println("You've been accepted");
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
    public synchronized void didLogin(Player p1, Player p2)
    {
        player = p2;

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

}
