package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ServerAdapter implements Runnable
{
    private enum Commands {
        LOGIN,
        PRINT_BOARD,
        STOP
    }
    private Commands nextCommand;
    private Player player;
    private Socket server;
    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;
    private Object lockClient = new Object();

    private List<ServerObserver> observers = new ArrayList<>();


    public ServerAdapter(Socket server)
    {
        this.server = server;
    }


    public void addObserver(ServerObserver observer)
    {
        synchronized (observers) {
            observers.add(observer);
        }
    }


    public void removeObserver(ServerObserver observer)
    {
        synchronized (observers) {
            observers.remove(observer);
        }
    }


    public void stop()
    {
        nextCommand = Commands.STOP;
        synchronized (lockClient){
            lockClient.notifyAll();
        }
    }

    public void printBoard()
    {
        nextCommand = Commands.PRINT_BOARD;
        synchronized (lockClient){
            lockClient.notifyAll();
        }
    }


    public void login(Player p) {
        nextCommand = Commands.LOGIN;
        player=p;
        synchronized (lockClient){
            lockClient.notifyAll();
        }
    }


    @Override
    public void run()
    {
        try {
            outputStm = new ObjectOutputStream(server.getOutputStream());
            inputStm = new ObjectInputStream(server.getInputStream());
            handleServerConnection();
        } catch (IOException e) {
            System.out.println("server has died");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("protocol violation");
        }

        try {
            server.close();
        } catch (IOException e) { }
    }


    private void handleServerConnection() throws IOException, ClassNotFoundException
    {
        /* wait for commands */
        while (true) {
            nextCommand = null;
            try {
                synchronized (lockClient){
                    lockClient.wait();
                }

            } catch (InterruptedException e) { }

            if (nextCommand == null)
                continue;

            switch (nextCommand) {
                case LOGIN:
                    doLogin();
                    break;

                case PRINT_BOARD:
                    doPrintBoard();
                    break;

                case STOP:
                    return;
            }
        }
    }


    private synchronized void doLogin() throws IOException, ClassNotFoundException
    {
        outputStm.writeObject(player);
        Player p2 = (Player)inputStm.readObject();
        //System.out.println(p.toString());
        /* copy the list of observers in case some observers changes it from inside
         * the notification method */
        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didLogin(player, p2);
        }
    }

    private synchronized void doPrintBoard() throws IOException, ClassNotFoundException
    {
        Object obj = inputStm.readObject();
        String board = (String)obj;

        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didReceiveBoard(board);
        }
    }
}