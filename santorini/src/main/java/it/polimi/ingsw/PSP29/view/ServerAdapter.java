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
        GET_MESSAGE,
        SERVICE_MESSAGE,
        INTERACTION_SERVER,
        STOP
    }
    private Commands nextCommand;
    private String cmd;
    private boolean connected = false;
    private boolean CLI = true;
    private GUI gui;
    private Socket server;
    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;

    private List<ServerObserver> observers = new ArrayList<>();


    public ServerAdapter(Socket server, boolean cli)
    {
        this.server = server;
        if(!cli){
            CLI = false;
            gui = new GUI();
        }
    }


    /**
     * add an observer to the list
     * @param observer
     */
    public void addObserver(ServerObserver observer)
    {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * remove an observer from the list
     * @param observer
     */
    public void removeObserver(ServerObserver observer)
    {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * set nextCommand to INTERACTION_SERVER
     * @param cmd the string to print
     */
    public synchronized void interactionServer(String cmd)
    {
        nextCommand = Commands.INTERACTION_SERVER;
        this.cmd = cmd;
        notifyAll();
    }

    /**
     * set nextCommand to SERVICE_MESSAGE
     * @param cmd the string to print
     */
    public synchronized void serviceMessage(String cmd)
    {
        nextCommand = Commands.SERVICE_MESSAGE;
        this.cmd = cmd;
        notifyAll();
    }


    /**
     * set nextCommand to GET_MESSAGE
     */
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

    /**
     * control nextCommand and call the others methods
     * @throws IOException if client disconnected
     * @throws ClassNotFoundException
     */
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

    /**
     * execution of the command INTERACTION_SERVER
     */
    public synchronized void doInteractionServer(){
        String rsp = null;
        if(CLI){
            Scanner s = new Scanner(System.in);
            System.out.print(cmd);
            rsp = s.nextLine();
        }else{
            if(cmd.startsWith("LOGI")){
                gui.doLogin(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
                rsp = gui.getMessage();
            }
            if(cmd.startsWith("LOBB")){
                gui.doLobby(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
                rsp = gui.getMessage();
            }
        }
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

    /**
     * execution of the command SERVICE_MESSAGE
     */
    public synchronized void doServiceMessage(){
        String rsp = null;
        if(CLI){
            System.out.print(cmd);
        }else{
            if(cmd.substring(0,3).equals("BORD")){
                gui.viewBoard(cmd);
            }
            if(cmd.startsWith("LIST")) {
                //gui.viewList(cmd);
            }
            if(cmd.startsWith("LSTP")){
                //gui.viewListPlayer(cmd);
            }
            if(cmd.startsWith("MSGE")){
                gui.viewMessage(cmd);
            }
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

    /**
     * execution of the command GET_MESSAGE
     * @throws IOException if client disconnected
     * @throws ClassNotFoundException if cast doesn't work
     */
    private synchronized void doGetMessage() throws IOException, ClassNotFoundException
    {
        /* send the string to the server and get the new string back */
        String newStr1 = (String)inputStm.readObject();
        String newStr2= (String)inputStm.readObject();
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

    /**
     * control if client is connected to the server
     * @return true if connected
     */
    public synchronized boolean getConnected(){
        return connected;
    }

}