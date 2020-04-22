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
        int maxPlayers = 0;
        System.out.println("server ready");
        while(true){
            if(countPlayers==0){
                try {
                    Socket client = socket.accept();
                    ClientHandler clientHandler = new ClientHandler(client, gameController);
                    Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
                    thread.start();
                    while(!clientHandler.didHandleConnection()){ }
                    clientHandler.sendMessage("\nInsert nickname and age: ");
                    while(!clientHandler.didSend()){ }
                    clientHandler.accept(true);
                    while (!clientHandler.didAccept()){  }
                    maxPlayers = clientHandler.doCreateLobby();
                    while(!clientHandler.didCreateLobby()){  }
                    clientHandlers.add(clientHandler);
                    System.out.println(gameController.getMatch().getPlayers().toString());
                    countPlayers++;
                } catch (IOException e) {
                    System.out.println("non valido");
                }
            }
            while(countPlayers<maxPlayers){
                try {
                    Socket client = socket.accept();
                    ClientHandler clientHandler = new ClientHandler(client, gameController);
                    Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
                    thread.start();
                    while(!clientHandler.didHandleConnection()){ }
                    clientHandler.sendMessage("\nInsert nickname and age: ");
                    while(!clientHandler.didSend()){ }
                    clientHandler.accept(false);
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                } catch (IOException e) {
                    System.out.println("non valido");
                }
            }
            if(countPlayers==maxPlayers){
                for(ClientHandler clientHandler : clientHandlers){
                    while(!clientHandler.didAccept()){  }
                }
            }
            System.out.println("All players are in");
            for(ClientHandler cH : clientHandlers){
                cH.doPrintBoard();
            }
            if(countPlayers==maxPlayers){
                for(ClientHandler clientHandler : clientHandlers){
                    while(!clientHandler.didPrintBoard()){  }
                }
            }
            for(ClientHandler cH : clientHandlers){
                cH.resetBoardPrinted();
            }
            System.out.println("Gameboard printed" + "\n\n\n");
            break;
        }
    }

}
