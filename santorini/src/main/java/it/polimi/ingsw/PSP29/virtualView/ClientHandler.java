package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.controller.GameController;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.view.ServerAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class ClientHandler implements Runnable
{
    private enum Commands {
        SEND_MESSAGE,
        TAKE_MESSAGE,
        STOP
    }
    private Commands nextCommand;
    private Socket client;
    private boolean login;
    private GameController gc;
    private String message;
    private String method;
    private boolean connected;
    private boolean sentMessage;
    private boolean readMessage;
    ObjectOutputStream output;
    ObjectInputStream input;

    ClientHandler(Socket client, GameController gc)
    {
        this.client = client;
        this.gc = gc;
    }


    @Override
    public void run()
    {
        try {
            login = false;
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }

    public synchronized void takeMessage()
    {
        nextCommand = Commands.TAKE_MESSAGE;
        notifyAll();
    }

    public synchronized void sendMessage(String meth, String msg)
    {
        nextCommand = Commands.SEND_MESSAGE;
        method = meth;
        message = msg;
        notifyAll();
    }


    private synchronized void handleClientConnection() throws IOException
    {
        connected = true;
        while (true) {
            nextCommand = null;
            try {
                wait();
            } catch (InterruptedException e) { }

            if (nextCommand == null)
                continue;

            switch (nextCommand) {
                case TAKE_MESSAGE:
                    doTakeMessage();
                    break;

                case SEND_MESSAGE:
                    doSendMessage();
                    break;

                case STOP:
                    return;
            }
        }

    }

    public synchronized void doTakeMessage(){
        readMessage = true;
        try {
            message = (String) input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean getConnected() {
        return connected;
    }

    public synchronized boolean getSentMessage() {
        return sentMessage;
    }

    public synchronized boolean getReadMessage() {
        return readMessage;
    }

    public synchronized void doSendMessage() {
        sentMessage = true;
        try {
            output.writeObject(method);
            output.writeObject(message);
        } catch (IOException e) {
            System.out.println("Not valid");
        }
    }

    public synchronized String getMessage(){
        return message;
    }

    public synchronized void resetReadMessage(){
        readMessage = false;
    }

    public synchronized void resetSentMessage(){
        sentMessage = false;
    }

}