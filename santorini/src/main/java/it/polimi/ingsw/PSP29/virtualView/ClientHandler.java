package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class ClientHandler implements Runnable
{
    private Socket client;


    ClientHandler(Socket client)
    {
        this.client = client;
    }


    @Override
    public void run()
    {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }


    private void handleClientConnection() throws IOException
    {
        System.out.println("Connected to " + client.getInetAddress());

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());

        try {
            while (true) {
                try{
                    output.writeObject("Inserire prima Username e poi eta': \n");
                    output.flush();
                    Player p = (Player)input.readObject();
                    p.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Dio bastardo");
                }
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from client");
        }

        client.close();
    }
}
