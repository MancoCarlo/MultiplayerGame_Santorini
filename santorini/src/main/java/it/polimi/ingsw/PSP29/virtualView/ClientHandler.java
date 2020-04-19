package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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


    private void handleClientConnection() throws IOException {
        System.out.println("Connected to " + client.getInetAddress());

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());
        try{
            while(true){
                Object next = input.readObject();
                Player player = (Player)next;
                System.out.println(player.toString());
                output.writeObject(player);
            }

        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("non valido");
        }

        client.close();
    }
}
