package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.virtualView.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Scanner;

import it.polimi.ingsw.PSP29.model.Color;


public class Client implements Runnable, ServerObserver
{
    /**
     * message from the server
     */
    private String response = null;

    /**
     * method that must be invoked
     */
    private String method = null;

    /**
     * message that must be sent to the server
     */
    private boolean rsp = false;

    /**
     * ip of the server
     */
    private String ip;


    @Override
    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        boolean CLI = false;
        System.out.println("What Interface you want to use?\n1) CLI\n2) GUI");
        String answer = scanner.nextLine();
        while(!answer.equals("1") && !answer.equals("2")){
            System.out.print("Input not valid, try again: ");
            answer = scanner.nextLine();
        }
        if(answer.equals("1")){
            CLI = true;
        }
        System.out.println("Insert server's IP");
        ip = scanner.nextLine();

        /* open a connection to the server */
        Socket server;
        try {
            server = new Socket(ip, Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected and waiting for a valid lobby");

        /* Create the adapter that will allow communication with the server
         * in background, and start running its thread */
        ServerAdapter serverAdapter = new ServerAdapter(server,CLI);
        serverAdapter.addObserver(this);
        Thread serverAdapterThread = new Thread(serverAdapter);
        serverAdapterThread.start();
        while(!serverAdapter.getConnected()) { }

        while (true) {

            synchronized (this) {
                response = null;
                method = null;
                rsp = false;
                System.out.print("\033[H\033[2J");
                serverAdapter.getMessage();
                while (response == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                }
                Method method1;
                try {
                    method1 = ServerAdapter.class.getMethod(method, String.class);
                    method1.invoke(serverAdapter, response);
                    while(!rsp) wait();
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InterruptedException e) {
                    e.printStackTrace();
                }
                if(method.equals("Stop")) break;
            }
        }
        System.out.println("close");
    }


    /**
     * control if the cliend received the message
     * @param newStr1 the message
     * @param newStr2 the method that will be called
     */
    @Override
    public synchronized void didReceiveMessage(String newStr1, String newStr2)
    {
        response = newStr2;
        method = newStr1;
        notifyAll();
    }

    /**
     * control if the serverAdapter method has been executed
     * @param rsp true if executed
     */
    @Override
    public synchronized void didInvoke(boolean rsp)
    {
        this.rsp = rsp;
        notifyAll();
    }
}
