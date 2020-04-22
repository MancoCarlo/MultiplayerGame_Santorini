package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.*;

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
        LOBBY,
        PRINT_BOARD,
        READ_MESSAGE,
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

    public synchronized void readMessage()
    {
        nextCommand = Commands.READ_MESSAGE;
        notifyAll();
    }

    public synchronized void printBoard()
    {
        nextCommand = Commands.PRINT_BOARD;
        notifyAll();
    }

    public synchronized void lobby()
    {
        nextCommand = Commands.LOBBY;
        notifyAll();
    }

    public synchronized void login(Player p) {
        nextCommand = Commands.LOGIN;
        player=p;
        notifyAll();
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


    private synchronized void handleServerConnection() throws IOException, ClassNotFoundException
    {
        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didHandleConnection();
        }
        /* wait for commands */
        while (true) {
            nextCommand = null;

            try {
                wait();

            } catch (InterruptedException e) { }

            if (nextCommand == null)
                continue;

            switch (nextCommand) {
                case LOGIN:
                    doLogin();
                    break;

                case READ_MESSAGE:
                    doRead();
                    break;

                case LOBBY:
                    createLobby();
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
        boolean f = (boolean)inputStm.readObject();
        //System.out.println(p.toString());
        /* copy the list of observers in case some observers changes it from inside
         * the notification method */
        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didLogin(player, p2, f);
        }
    }

    private synchronized void doRead() throws IOException, ClassNotFoundException
    {
        Object obj = inputStm.readObject();
        String message = (String)obj;

        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        for (ServerObserver observer: observersCpy) {
            observer.didRead(message);
        }
    }

    private synchronized void createLobby() throws IOException, ClassNotFoundException
    {
        System.out.println("How many players: 2 or 3?");
        Scanner scanner = new Scanner(System.in);
        int numP = scanner.nextInt();
        outputStm.writeObject(numP);

        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didLobby();
        }
    }



    private synchronized void doPrintBoard() throws IOException, ClassNotFoundException
    {
        Object obj = inputStm.readObject();
        Box[][] board = (Box[][])obj;

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