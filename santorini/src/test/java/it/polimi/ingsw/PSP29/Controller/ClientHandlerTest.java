package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.virtualView.ClientHandler;

import java.io.*;

public class ClientHandlerTest extends ClientHandler {
    private boolean sentMessage = false;
    private boolean readMessage = false;
    private boolean connected = true;
    private String message = null;
    private String name = "";
    public BufferedReader b;

    public ClientHandlerTest(String path) throws FileNotFoundException {
        FileReader fr = new FileReader(path);
        b=new BufferedReader(fr);
    }

    public synchronized void takeMessage()
    {
        readMessage = true;
    }

    public void sendMessage(String met, String msg)
    {
        sentMessage = true;
    }

    public boolean getConnected(){ return connected; }

    public boolean getSentMessage() {
        return sentMessage;
    }

    public boolean getReadMessage() {
        return readMessage;
    }

    public String getMessage(){
        try {
            message = b.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void resetReadMessage(){
        readMessage = false;
    }

    public void resetSentMessage(){
        sentMessage = false;
    }

    public String getName(){
        return name;
    }

    public void setName(String n) {
        name=n;
    }

    @Override
    public void closeConnection() {
    }
}
