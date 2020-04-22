package it.polimi.ingsw.PSP29.virtualView;


import it.polimi.ingsw.PSP29.controller.GameController;
import it.polimi.ingsw.PSP29.model.*;

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
                    ClientHandler clientHandler=null;
                    while (maxPlayers==0){
                        Socket client = socket.accept();
                        clientHandler = new ClientHandler(client, gameController);
                        Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
                        thread.start();
                        while(!clientHandler.didHandleConnection()){ }
                        clientHandler.doSend("\nInsert nickname and age: ");
                        while(!clientHandler.didSend()){ }
                        clientHandler.doAccept(true);
                        while (!clientHandler.didAccept()){  }
                        maxPlayers = clientHandler.doCreateLobby();
                        while(!clientHandler.didCreateLobby()){  }
                    }
                    clientHandler.resetDidSend();
                    clientHandler.doSend("true");
                    while(!clientHandler.didSend()){ }
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                } catch (IOException e) {
                    System.out.println("non valido");
                }
            }
            boolean add;
            boolean message;
            boolean connection;
            while(countPlayers<maxPlayers){
                add=false;
                message=false;
                connection=false;
                while(!add || !message || !connection){
                    ClientHandler clientHandler=null;
                    try{
                        Socket client = socket.accept();
                        clientHandler = new ClientHandler(client, gameController);
                        Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
                        thread.start();
                    } catch (IOException e){
                        System.out.println("disconnected");
                        continue;
                    }
                    connection=clientHandler.handleClientConnection();
                    while(!clientHandler.didHandleConnection()){ }
                    if(connection){
                        message=clientHandler.doSend("\nInsert nickname and age: ");
                        while(!clientHandler.didSend()){ }
                        if(message){
                            add=clientHandler.doAccept(false);
                            clientHandlers.add(clientHandler);
                        }
                    }
                }
                countPlayers++;
            }
            if(countPlayers==maxPlayers){
                for(ClientHandler clientHandler : clientHandlers){
                    while(!clientHandler.didAccept()){  }
                }
            }
            System.out.println("All players are in");
            for(ClientHandler cH : clientHandlers){
                cH.resetDidAccept();
            }

            System.out.println(gameController.getMatch().getPlayers().toString());
            String duplicate = gameController.getMatch().findDuplicate();
            System.out.println(duplicate);
            boolean find=false;
            int n=0;
            while (duplicate!=null){
                for(int i=1; i<clientHandlers.size(); i++){
                    find=false;
                    if(duplicate.equals(clientHandlers.get(i).getNickname())){
                        find=true;
                        clientHandlers.get(i).resetDidSend();
                        clientHandlers.get(i).doSend("false");
                        while(!clientHandlers.get(i).didSend()){ }
                        clientHandlers.get(i).resetDidSend();
                        clientHandlers.get(i).doSend("Your nickname is already used" + "\n" + "Insert new nickname and age");
                        while(!clientHandlers.get(i).didSend()){ }
                        clientHandlers.get(i).resetDidAccept();
                        clientHandlers.get(i).doAccept(false);
                        while(!clientHandlers.get(i).didAccept()){  }
                    }
                    if (!find){
                        clientHandlers.get(i).resetDidSend();
                        clientHandlers.get(i).doSend("true");
                        while(!clientHandlers.get(i).didSend()){ }
                        n=i;
                    }
                }
                duplicate = gameController.getMatch().findDuplicate();
            }
            if(n==0 && clientHandlers.size()==2){
                clientHandlers.get(1).resetDidSend();
                clientHandlers.get(1).doSend("true");
                while(!clientHandlers.get(1).didSend()){ }
            }
            else if(clientHandlers.size()==3){
                if(n==0){
                    clientHandlers.get(1).resetDidSend();
                    clientHandlers.get(1).doSend("true");
                    while(!clientHandlers.get(1).didSend()){ }
                    clientHandlers.get(2).resetDidSend();
                    clientHandlers.get(2).doSend("true");
                    while(!clientHandlers.get(2).didSend()){ }
                }
                else{
                    clientHandlers.get(clientHandlers.size()-n).resetDidSend();
                    clientHandlers.get(clientHandlers.size()-n).doSend("true");
                    while(!clientHandlers.get(clientHandlers.size()-n).didSend()){ }
                }

            }
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
