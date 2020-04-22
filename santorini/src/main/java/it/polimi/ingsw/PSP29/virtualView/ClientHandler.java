package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.controller.GameController;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.view.ServerAdapter;
import it.polimi.ingsw.PSP29.view.ServerObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ClientHandler implements Runnable
{
    private enum Commands{
        ACCEPT,
        LOBBY,
        PRINT_BOARD,
        SEND_MESSAGE
    }

    Commands nextCommand;
    private Socket client;
    private GameController GC;
    private boolean connection;
    private boolean accept;
    private boolean boardPrinted;
    private boolean lobbyCreated;
    private boolean sentMessage;
    private Player player;

    ObjectOutputStream output;
    ObjectInputStream input;

    public ClientHandler(Socket client, GameController gameController) {
        this.client = client;
        GC = gameController;
    }

    public String getNickname(){
        return player.getNickname();
    }


    public synchronized void printBoard() {
        nextCommand = Commands.PRINT_BOARD;
        notifyAll();
    }

    public synchronized void createLobby() {
        nextCommand = Commands.LOBBY;
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
            lobbyCreated=false;
            accept=false;
            boardPrinted=false;
            sentMessage = false;
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }


    public synchronized boolean handleClientConnection(){
        connection=true;
        while (true) {
            nextCommand = null;
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("disconnected");
            }
            if (nextCommand == null){
                continue;
            }

            switch (nextCommand) {

                case PRINT_BOARD:
                    doPrintBoard();
                    break;

                case LOBBY:
                    doCreateLobby();
                    break;
            }
        }

    }

    public synchronized boolean doAccept(boolean bool){
        accept=true;
        try{
            Object next = input.readObject();
            Player p = (Player) next;
            player=p;
            output.writeObject(p);
            output.writeObject(bool);
            GC.getMatch().addPlayer(player);
            return true;
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("non valido");
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public synchronized boolean doSend(String msg){
        sentMessage = true;
        try {
            output.writeObject(msg);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public synchronized void doPrintBoard(){
        boardPrinted=true;
        try{
            Box board[][] = GC.getMatch().getBoard();
            output.writeObject(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized int doCreateLobby(){
        lobbyCreated=true;
        try{
            Object obj = input.readObject();
            return (int)obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public synchronized boolean didHandleConnection(){
        return connection;
    }

    public synchronized boolean didAccept(){
        return accept;
    }

    public synchronized boolean didSend(){
        return sentMessage;
    }

    public synchronized boolean didPrintBoard(){
        return boardPrinted;
    }

    public synchronized boolean didCreateLobby(){
        return lobbyCreated;
    }

    public synchronized void resetDidAccept(){
        accept=false;
    }

    public synchronized void resetDidSend() { sentMessage=false; }

    public synchronized void resetBoardPrinted(){
        boardPrinted=false;
    }
}
