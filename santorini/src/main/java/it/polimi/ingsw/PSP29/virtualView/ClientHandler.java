package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.Controller.GameController;
import it.polimi.ingsw.PSP29.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;


public class ClientHandler implements Runnable
{
    private enum Commands {
        SEND_MESSAGE,
        TAKE_MESSAGE,
        STOP
    }
    private Commands nextCommand;
    private Socket client;
    private String name;
    private int age;
    private String message;
    private String method;
    private boolean connected;
    private boolean sentMessage;
    private boolean readMessage;
    ObjectOutputStream output;
    ObjectInputStream input;
    private Server server;

    ClientHandler(Socket client, Server server)
    {
        this.client = client;
        this.server = server;
    }


    @Override
    public void run()
    {
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }

    /**
     * set nextCommand to TAKE_MESSAGE
     */
    public synchronized void takeMessage()
    {
        nextCommand = Commands.TAKE_MESSAGE;
        notifyAll();
    }

    /**
     * set nextCommand to SEND_MESSAGE
     */
    public synchronized void sendMessage(String met, String msg)
    {
        nextCommand = Commands.SEND_MESSAGE;
        method = met;
        message = msg;
        notifyAll();
    }

    /**
     * control nextCommand and call the others methods
     */
    private synchronized void handleClientConnection()
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

    /**
     * take a message from the client
     */
    public synchronized void doTakeMessage(){
        readMessage = true;
        UserTimerTask ut = new UserTimerTask(server, this);
        Timer timer = new Timer();
        timer.schedule(ut, 0, 1000);
        try {
            message = (String) input.readObject();
            timer.cancel();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(name + " - read - client disconnected");
            message = null;
            resetConnected();
        }
    }

    /**
     * control if the client is connected
     * @return true if connected
     */
    public synchronized boolean getConnected() {
        return connected;
    }

    /**
     * control if the message has been sent
     * @return true if sent
     */
    public synchronized boolean getSentMessage() {
        return sentMessage;
    }

    /**
     * control if the message has been read
     * @return true if read
     */
    public synchronized boolean getReadMessage() {
        return readMessage;
    }

    /**
     * send a message to the client
     */
    public synchronized void doSendMessage() {
        sentMessage = true;
        try {
            output.flush();
            output.writeObject(method);
            output.writeObject(message);
        } catch (IOException e) {
            System.out.println(name + " - send - client disconnected");
            resetConnected();
        }
    }

    /**
     * @return the message to the server
     */
    public synchronized String getMessage(){
        return message;
    }

    /**
     * reset readMessage
     */
    public synchronized void resetReadMessage(){
        readMessage = false;
    }

    /**
     * reset sentMessage
     */
    public synchronized void resetSentMessage(){
        sentMessage = false;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int a) {
        age=a;
    }

    public String getName(){
        return name;
    }

    public void setName(String n) {
        name=n;
    }

    public void resetConnected(){
        connected = false;
    }

    public void closeConnection(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}