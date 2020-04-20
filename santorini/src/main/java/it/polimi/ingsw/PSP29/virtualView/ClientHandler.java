package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.controller.GameController;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.view.ServerAdapter;
import it.polimi.ingsw.PSP29.view.ServerObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ClientHandler implements Runnable
{
    private enum Commands{
        ACCEPT,
        PRINT_BOARD
    }

    Commands nextCommand;
    private Socket client;
    private GameController GC;
    private boolean connection;
    private boolean accept;

    ObjectOutputStream output;
    ObjectInputStream input;

    public ClientHandler(Socket client, GameController gameController) {
        this.client = client;
        GC = gameController;
    }

    public synchronized  void accept() {
        nextCommand = Commands.ACCEPT;
        System.out.println("accept");
        notifyAll();
    }

    public synchronized void printBoard() {
        nextCommand = Commands.PRINT_BOARD;
        notifyAll();
    }

    @Override
    public void run()
    {
        try {
            System.out.println("Connected to " + client.getInetAddress());
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            connection=false;
            accept=false;
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }


    private synchronized void handleClientConnection(){
        System.out.println("handleclientconnection");
        connection=true;
        while (true) {
            nextCommand = null;
            try {
                wait();
            } catch (InterruptedException e) { }
            System.out.println("svegliato");
            if (nextCommand == null){
                System.out.println("nextCommand null");
                continue;
            }

            System.out.println("case");
            switch (nextCommand) {
                case ACCEPT:
                    doAccept();
                    break;
            }
        }

    }

    public synchronized void doAccept(){
        accept=true;
        try{
            System.out.println("doaccept");
                Object next = input.readObject();
                Player player = (Player) next;
                output.writeObject(player);
                GC.getMatch().addPlayer(player);
        } catch (ClassCastException | ClassNotFoundException | IOException e) {
            System.out.println("non valido");
        }

    }

    public synchronized void doPrintBoard(){
        try{
            String b = GC.getMatch().getBoard().toString();
            output.writeObject(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean didHandleConnection(){
        if(connection){
            System.out.println("connected");
        }
        return connection;
    }

    public synchronized boolean didAccept(){
        return accept;
    }
}
