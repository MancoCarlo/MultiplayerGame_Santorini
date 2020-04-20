package it.polimi.ingsw.PSP29.virtualView;


import it.polimi.ingsw.PSP29.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server
{
    public final static int SOCKET_PORT = 7777;
    GameController gameController;


    public void serverExe(GameController gc) {
        GameController gameController = gc;
        ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        ServerSocket socket;
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            return;
        }
        int countPlayers = 0;
        System.out.println("server ready");
        while(true){
            while(countPlayers<3){
                try {
                    Socket client = socket.accept();
                    ClientHandler clientHandler = new ClientHandler(client, gameController);
                    Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
                    thread.start();
                    while(!clientHandler.didHandleConnection()){
                    }
                    clientHandler.accept();
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                } catch (IOException e) {
                    System.out.println("non valido");
                }
            }
            while(countPlayers==3 && !(clientHandlers.get(0).didAccept() && clientHandlers.get(1).didAccept() && clientHandlers.get(2).didAccept())){  }
            System.out.println("All players are in");
            for(ClientHandler cH : clientHandlers){
                cH.doPrintBoard();
            }
            while(countPlayers==3 && !(clientHandlers.get(0).didPrintBoard() && clientHandlers.get(1).didPrintBoard() && clientHandlers.get(2).didPrintBoard())){  }
            for(ClientHandler cH : clientHandlers){
                cH.resetBoardPrinted();
            }
            System.out.println("Gameboard printed" + "\n\n\n");
            break;
        }
    }

}
