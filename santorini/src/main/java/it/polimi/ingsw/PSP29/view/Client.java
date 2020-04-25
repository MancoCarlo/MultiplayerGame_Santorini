package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.virtualView.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Scanner;


public class Client implements Runnable, ServerObserver
{
    /* auxiliary variable used for implementing the consumer-producer pattern*/
    private Object response = null;
    private String method = null;
    private String message = null;
    private boolean rsp = false;

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

        String ip = "127.0.0.8";

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
        ServerAdapter serverAdapter = new ServerAdapter(server);
        serverAdapter.addObserver(this);
        Thread serverAdapterThread = new Thread(serverAdapter);
        serverAdapterThread.start();
        while(!serverAdapter.getConnected()) { }

        while (true) {

            synchronized (this) {
                response = null;
                method = null;
                rsp = false;
                serverAdapter.getMessage();
                while (response == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                }
                Method method1;
                try {
                    switch (method){
                        case "serviceMessage":
                        case "interactionServer":
                            message = (String)response;
                            method1 = ServerAdapter.class.getMethod(method, String.class);
                            method1.invoke(serverAdapter, message);
                            while(!rsp) wait();
                            break;
                        case "printObject":
                            method1 = ServerAdapter.class.getMethod(method, Object.class);
                            method1.invoke(serverAdapter, response);
                            while(!rsp) wait();
                            break;
                    }

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public synchronized void didReceiveMessage(String newStr1, Object obj)
    {
        response = obj;
        method = newStr1;
        notifyAll();
    }

    @Override
    public synchronized void didInvoke(boolean rsp)
    {
        this.rsp = rsp;
        notifyAll();
    }
}
