package it.polimi.ingsw.PSP29.view;

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
        GET_MESSAGE,
        SERVICE_MESSAGE,
        INTERACTION_SERVER,
        STOP;

    }
    private Commands nextCommand;
    private String cmd;
    private boolean connected = false;

    private Socket server;
    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;

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

    public synchronized void interactionServer(String cmd)
    {
        nextCommand = Commands.INTERACTION_SERVER;
        this.cmd = cmd;
        notifyAll();
    }

    public synchronized void serviceMessage(String cmd)
    {
        nextCommand = Commands.SERVICE_MESSAGE;
        this.cmd = cmd;
        notifyAll();
    }


    public synchronized void getMessage()
    {
        nextCommand = Commands.GET_MESSAGE;
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
        /* wait for commands */
        connected = true;
        while (true) {
            nextCommand = null;

            try {
                wait();
            } catch (InterruptedException e) { }

            if (nextCommand == null)
                continue;
            System.out.println(nextCommand);
            switch (nextCommand) {
                case GET_MESSAGE:
                    doGetMessage();
                    break;

                case INTERACTION_SERVER:
                    doInteractionServer();
                    break;

                case SERVICE_MESSAGE:
                    doServiceMessage();
                    break;

                case STOP:
                    return;
            }
        }
    }

    public synchronized void doInteractionServer(){
        Scanner s = new Scanner(System.in);
        System.out.print(cmd);
        String rsp = s.nextLine();
        try {
            outputStm.writeObject(rsp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didInvoke(true);
        }
    }

    public synchronized void doServiceMessage(){
        System.out.print(cmd);

        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didInvoke(true);
        }
    }

    private synchronized void doGetMessage() throws IOException, ClassNotFoundException
    {
        /* send the string to the server and get the new string back */
        String newStr1 = (String)inputStm.readObject();
        String newStr2 = (String)inputStm.readObject();
        /* copy the list of observers in case some observers changes it from inside
         * the notification method */
        List<ServerObserver> observersCpy;
        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ServerObserver observer: observersCpy) {
            observer.didReceiveMessage(newStr1, newStr2);
        }
    }

    public synchronized boolean getConnected(){
        return connected;
    }

}