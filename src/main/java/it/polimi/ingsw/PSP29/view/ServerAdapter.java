package it.polimi.ingsw.PSP29.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import it.polimi.ingsw.PSP29.model.Color;
import it.polimi.ingsw.PSP29.view.GUI.GUI;


public class ServerAdapter implements Runnable
{
    /**
     * commands that manage the server adapter
     */
    private enum Commands {
        GET_MESSAGE,
        SERVICE_MESSAGE,
        INTERACTION_SERVER,
        STOP
    }

    /**
     * the new command for the server adapter
     */
    private Commands nextCommand;

    /**
     * the message received from the server
     */
    private String cmd;

    /**
     * tell if the serveradapter is connected
     */
    private boolean connected = false;

    /**
     * true if the cli is active
     */
    private boolean CLI = true;

    /**
     * GUI of the client
     */
    private GUI gui;

    /**
     * the server
     */
    private Socket server;

    /**
     * output stream
     */
    private ObjectOutputStream outputStm;

    /**
     * input stream
     */
    private ObjectInputStream inputStm;

    /**
     * observer list
     */
    private List<ServerObserver> observers = new ArrayList<>();


    public ServerAdapter(Socket server, boolean cli){
        this.server = server;
        if(!cli){
            CLI = false;
            gui= new GUI();
            Thread threadGUI = new Thread(gui);
            threadGUI.start();
            while (!gui.getGuiLoaded()){
                System.out.print("");
            }
        }
    }


    /**
     * add an observer to the list
     * @param observer observer
     */
    public void addObserver(ServerObserver observer)
    {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * remove an observer from the list
     * @param observer observer
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

    /**
     * set nextCommand to STOP
     * @param cmd client's command
     */
    public synchronized void Stop(String cmd)
    {
        nextCommand = Commands.STOP;
        this.cmd = cmd;
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
        } catch (ClassCastException e) {
            System.out.println("protocol violation");
        }

        try {
            server.close();
        } catch (IOException e) { }
    }

    /**
     * control nextCommand and call the others methods
     * @throws IOException if client disconnected
     */
    private synchronized void handleServerConnection() throws IOException
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
                    doStop();
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
            if(cmd.startsWith("COOR")){
                System.out.println(cmd.substring(5));
                System.out.print("X: ");
                String x = s.nextLine();
                System.out.print("Y: ");
                String y = s.nextLine();
                rsp = x + y;
            }else{
                System.out.print(cmd.substring(5));
                rsp = s.nextLine();
            }
        }else{
            if(cmd.startsWith("LOGI")){
                gui.login(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
                rsp = gui.getMessage();
            }
            if(cmd.startsWith("LOBB")){
                gui.lobby(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
                rsp = gui.getMessage();
            }
            if(cmd.startsWith("INDX")){
                gui.index(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
                rsp = gui.getMessage();
            }
            if(cmd.startsWith("COOR")){
                gui.coordinate(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
                rsp = gui.getMessage();
            }
            if(cmd.startsWith("TURN")){
                gui.turn(cmd);
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
        if(CLI){
            if(cmd.startsWith("BORD")){
                ArrayList<String> board = new ArrayList<>();
                cmd = cmd.substring(5);
                int i = 0;
                while(i<cmd.length()){
                    board.add(cmd.substring(i,i+2));
                    i=i+2;
                }
                String gameboard = "Gameboard\n  \t";
                for(i=0; i<5; i++){
                    gameboard = gameboard + i + " \t";
                }
                gameboard = gameboard + "\n";
                for(i=0; i<5; i++){
                    gameboard = gameboard + i + " \t";
                    for(int j=0; j<5; j++){
                        String first;
                        String second;
                        switch(board.get(0).charAt(0)){
                            case '1':
                                first = ""+Color.ANSI_RED + board.get(0).charAt(0) + Color.RESET;
                                break;
                            case '2':
                                first = ""+Color.ANSI_BLUE + board.get(0).charAt(0) + Color.RESET;
                                break;
                            case '3':
                                first = ""+Color.ANSI_YELLOW + board.get(0).charAt(0) + Color.RESET;
                                break;
                            default:
                                first = ""+board.get(0).charAt(0);
                                break;
                        }
                        switch(board.get(0).charAt(1)){
                            case '1':
                                second = ""+Color.ANSI_LEVEL1 + board.get(0).charAt(1) + Color.RESET;
                                break;
                            case '2':
                                second = ""+Color.ANSI_LEVEL2 + board.get(0).charAt(1) + Color.RESET;
                                break;
                            case '3':
                                second = ""+Color.ANSI_LEVEL3 + board.get(0).charAt(1) + Color.RESET;
                                break;
                            case '4':
                                second = ""+Color.ANSI_LEVEL4 + board.get(0).charAt(1) + Color.RESET;
                                break;
                            default:
                                second = ""+board.get(0).charAt(1);
                                break;
                        }
                        gameboard = gameboard + first + second + "\t";
                        board.remove(0);
                    }
                    gameboard = gameboard + "\n";
                }
                cmd=gameboard;
                System.out.print(cmd);
            }else if(cmd.startsWith("MGOD") || cmd.startsWith("STOP")){
            }else{
                System.out.print(cmd.substring(5));
            }
        }else{
            if(cmd.startsWith("BORD")){
                gui.board(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
            }
            if(cmd.startsWith("LIST")) {
                gui.list(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
            }
            if(cmd.startsWith("LSTP")){
                gui.listPlayers(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
            }
            if(cmd.startsWith("MSGE")){
                gui.message(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
            }
            if(cmd.startsWith("MGOD")){
                gui.viewGod(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
            }
            if(cmd.startsWith("WINM")){
                gui.win(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
            }
            if(cmd.startsWith("STOP")){
                gui.stop(cmd);
                while(!gui.didSentMessage()){ }
                gui.resetSentMessage();
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
     */
    private synchronized void doGetMessage() throws IOException
    {
        /* send the string to the server and get the new string back */
        String newStr1 = null;
        try {
            newStr1 = (String)inputStm.readObject();
        } catch (ClassNotFoundException ignored) {
        }
        String newStr2= null;
        try {
            newStr2 = (String)inputStm.readObject();
        } catch (ClassNotFoundException ignored) {
        }
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

    private synchronized void doStop(){
        System.out.println(cmd);

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
     * control if client is connected to the server
     * @return true if connected
     */
    public synchronized boolean getConnected(){
        return connected;
    }

}