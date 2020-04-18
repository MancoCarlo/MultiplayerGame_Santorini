package it.polimi.ingsw.PSP29.virtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public final static int SOCKET_PORT = 6776;
    private static int numPlayer = 0;

    public static void main(String[] args)
    {
        ServerSocket socket;
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                do {
                    Socket client = socket.accept();
                    ClientHandler clientHandler = new ClientHandler(client);
                    Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                    thread.start();
                    numPlayer++;
                }while(numPlayer < 3);
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }
}
