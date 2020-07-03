package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.Controller.*;
import it.polimi.ingsw.PSP29.model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Luca Martiri, Carlo Manco
 */
public class Server
{
    /**
     * Port where the server work
     */
    public final static int SOCKET_PORT = 43123;

    /**
     * Controller that manage the game
     */
    private static GameController gc;

    /**
     * num of player in the match
     */
    private int numPlayers=0;

    /**
     * timeout for the interaction with client
     */
    private boolean timeout = false;

    /**
     * players currently logged
     */
    private int countPlayers = 0;

    /**
     * socket of the server
     */
    private ServerSocket socket;

    /**
     * array of clientHandlers
     */
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * boolean that tell the server if the client will play another match
     */
    private boolean playAgain;

    /**
     * server execution
     */
    public void launch(){
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }
        playAgain=true;
        launchMatch();
    }

    /**
     * start the match
     */
    public void launchMatch()
    {
        while (true) {
            System.out.println("server ready");
            System.out.println("Creating Lobby");
            while(true){
                playAgain=true;
                gc = new GameController(this);
                while(countPlayers==0){
                    ClientHandler clientHandler=null;
                    clientHandler = connection(socket, clientHandler);
                    if(loginPlayer(clientHandler)){
                        if(createLobby(clientHandler)){
                            while(numPlayers != 2 && numPlayers != 3){
                                if(!write(clientHandler, "serviceMessage", "MSGE-Players number not valid\n")){
                                    break;
                                }
                                if(!createLobby(clientHandler)){
                                    break;
                                }
                            }
                            if(numPlayers==2 || numPlayers==3){
                                write(clientHandler, "serviceMessage", "MSGE-\nWait for other players\n\n");
                                clientHandlers.add(clientHandler);
                                countPlayers++;
                            }
                            else{
                                gc.getMatch().getPlayers().remove(numPlayers);
                            }
                        }
                        else{
                            gc.getMatch().getPlayers().remove(numPlayers);
                        }
                    }
                }
                System.out.println("Adding players");
                while(countPlayers < numPlayers){
                    ClientHandler clientHandler = null;
                    clientHandler = connection(socket, clientHandler);
                    if(!loginPlayer(clientHandler)){
                        continue;
                    }
                    if(!write(clientHandler, "serviceMessage", "MSGE-\nWait for other players\n\n")){
                        gc.getMatch().getPlayers().remove(countPlayers);
                        continue;
                    }
                    clientHandlers.add(clientHandler);
                    countPlayers++;
                }

                gc.getMatch().updatePlayers(clientHandlers);

                gc.getMatch().sortPlayers();
                sortClientHandlers();

                for(Player p : gc.getMatch().getPlayers()){
                    p.setInGame(true);
                }

                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        if(!write(clientHandler, "serviceMessage", "MSGE-You're in\n\n")){
                            gc.getMatch().getPlayer(clientHandler.getName()).setInGame(false);
                        }
                    }
                }

                System.out.println("printing board");
                gc.getMatch().inizializeBoard();
                while (gc.getMatch().getBoard() == null){ }
                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage",  "BORD-"+gc.getMatch().printBoard());
                    }
                }

                System.out.println("printing players");
                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage", "LSTP-"+gc.getMatch().printPlayers());
                    }
                }

                if(!controlEndGame()) continue;

                System.out.println("Assigning gods");
                if(!gc.godsAssignement()){
                    playAgain=controlEndGame();
                }
                if(!playAgain) continue;


                String rightP = "";
                for(ClientHandler ch : clientHandlers){
                    rightP = rightP + ch.getName() + "," + gc.getMatch().getPlayer(ch.getName()).getCard().getName()+ "\n";
                }

                for(ClientHandler clientHandler : clientHandlers){
                    if(clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage",  "MGOD-"+clientHandler.getName()+";"+rightP);
                    }
                }

                System.out.println("Putting workers");
                if(!gc.putWorkers()){
                    playAgain=controlEndGame();
                }
                if(!playAgain) continue;

                gc.gameExe();


                newGame();
            }
        }
    }

    /**
     * control if a player disconnected from the server and start a new game
     * @return true if one player is disconnected
     */
    public boolean controlEndGame(){
        gc.getMatch().updatePlayers(clientHandlers);
        int count=0;
        for(ClientHandler clientHandler : clientHandlers){
            if(clientHandler.getConnected()){
                count++;
            }
        }

        if(count!=countPlayers){
            for(ClientHandler clientHandler : clientHandlers){
                {
                    if (clientHandler.getConnected()){
                        write(clientHandler, "serviceMessage" , "WINM-\nYou win!!\n");
                    }
                }
            }
            newGame();
            return false;
        }
        return true;
    }

    /**
     * create a new game at the end of the previous one
     */
    public void newGame(){
        ArrayList<ClientHandler> newCH = new ArrayList<>();
        for(int i = 0; i<clientHandlers.size();i++){
            if(clientHandlers.get(i).getConnected()){
                newCH.add(clientHandlers.get(i));
            }else{
                if(gc.getMatch().getPlayers().size()==0){
                    try {
                        this.socket.close();
                    } catch (IOException e) {
                        System.out.println("Server closed");
                    }
                }
                countPlayers--;
                clientHandlers.get(i).closeConnection();
            }
        }
        clientHandlers = newCH;

        for(int i=0; i<clientHandlers.size();i++){
            write(clientHandlers.get(i), "serviceMessage", "MSGE-Wait your turn\n");
        }

        for(int i=0; i<clientHandlers.size();i++){
            write(clientHandlers.get(i), "serviceMessage", "LIST-1) YES\n2) NO\n");
            write(clientHandlers.get(i),"interactionServer", "INDX-Would you like to play again?");
            String again;
            try{
                again = read(clientHandlers.get(i));
                if(again==null || again.equals("2")){
                    countPlayers--;
                    write(clientHandlers.get(i), "serviceMessage", "STOP");
                    write(clientHandlers.get(i), "Stop", "Closing connection");
                    clientHandlers.get(i).resetConnected();
                    clientHandlers.get(i).closeConnection();
                }
                else if(again.equals("1")){
                    write(clientHandlers.get(i), "serviceMessage", "MSGE-Wait the creation of a new game\n");
                }
            } catch (Exception e) {
                System.out.println("No connection");
            }
        }

        ArrayList<ClientHandler> CHs = new ArrayList<>();
        for(ClientHandler clientHandler : clientHandlers){
            if(clientHandler.getConnected()){
                CHs.add(clientHandler);
            }
        }
        clientHandlers = CHs;

        gc.getMatch().getPlayers().clear();
        for(int i=0; i<clientHandlers.size(); i++){
            gc.getMatch().getPlayers().add(new Player(clientHandlers.get(i).getName(), clientHandlers.get(i).getAge(), i+1));
        }

        for(int i=0; i<clientHandlers.size();i++){
            write(clientHandlers.get(i),"serviceMessage", "MSGE-Waiting for players\n");
        }

        if(clientHandlers.size()==0){
            playAgain = false;
        }
        else{
            playAgain = true;
        }

        launchMatch();
    }


    /**
     *
     * wait until the method is executed
     *
     * @param clientHandler that is interfaced with client
     * @param meth the method to process
     */
    public void process(ClientHandler clientHandler, String meth){
        try {
            Method method1 = ClientHandler.class.getMethod(meth);
            while(!(boolean)method1.invoke(clientHandler));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * reset the variable in clienthandler linked to the method passed
     *
     * @param clientHandler that is interfaced with client
     * @param meth the method to reset
     */
    public void processReset(ClientHandler clientHandler, String meth){
        try {
            Method method1 = ClientHandler.class.getMethod(meth);
            method1.invoke(clientHandler);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * accept a client and add him to the players list
     *
     * @param clientHandler that is interfaced with client
     * @return true if the client is logged correctly, else false
     */
    public boolean loginPlayer(ClientHandler clientHandler){

        if(!write(clientHandler,"interactionServer", "LOGI-Insert username: ")){
            return false;
        }
        String username = read(clientHandler);
        if(username==null){
            return false;
        }
        while(gc.getMatch().alreadyIn(username) || username.contains(",")){
            if(!write(clientHandler,"interactionServer", "LOGI-Username not valid, try again: ")){
                return false;
            }
            username = read(clientHandler);
            if(username==null){
                return false;
            }
        }

        int age;

        while(true){
            try{
                if(!write(clientHandler,"interactionServer", "LOGI-Insert age: ")){
                    return false;
                }
                String str = read(clientHandler);
                if(str==null){
                    return false;
                }
                age = Integer.parseInt(str);

                break;
            } catch (NumberFormatException e){
                if(!write(clientHandler, "serviceMessage", "LOGI-Invalid input\n")){
                    return false;
                }
                continue;
            }
        }

        clientHandler.setName(username);
        clientHandler.setAge(age);
        Player player1 = new Player(username, age, countPlayers+1);
        gc.getMatch().addPlayer(player1);
        gc.getMatch().getPlayer(username).setInGame(true);
        return true;
    }

    /**
     *
     * write a message to the client
     *
     * @param clientHandler that is interfaced with client
     * @param s the type of message
     * @param msg the message
     * @return true if the message is sended to Client correctly, else false
     */
    public boolean write(ClientHandler clientHandler, String s, String msg){
        clientHandler.sendMessage(s, msg);
        process(clientHandler, "getSentMessage");
        processReset(clientHandler, "resetSentMessage");
        if(!clientHandler.getConnected()){
            return false;
        }
        return true;
    }

    /**
     *
     * read a message from the client
     *
     * @param clientHandler that must read the message from his client
     * @return the message
     */
    public String read(ClientHandler clientHandler){
        clientHandler.takeMessage();
        try {
            Method method1 = ClientHandler.class.getMethod("getReadMessage");
            while(!(boolean)method1.invoke(clientHandler) && !timeout){  }
            if(timeout){
                timeout=false;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        processReset(clientHandler, "resetReadMessage");
        String response = null;
        if(clientHandler.getConnected()){
            response = clientHandler.getMessage();
        }
        if(response!=null){
            while(response.equals("")){
                write(clientHandler, "interactionServer", "ERRORNot valid input, try another input: ");
                response = read(clientHandler);
            }
        }
        return response;
    }

    /**
     *
     * connect a client to the server
     *
     * @param socket the server
     * @param clientHandler that must be connected to client
     * @return the clientHandler linked to the client
     */
    public ClientHandler connection(ServerSocket socket,ClientHandler clientHandler){
        Socket client;
        try {
            client = socket.accept();
            clientHandler = new ClientHandler();
            clientHandler.makeCH(client, this);
            Thread thread = new Thread(clientHandler , "server_" + client.getInetAddress());
            thread.start();
            process(clientHandler, "getConnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientHandler;
    }

    /**
     *
     * ask to the player how many players will be in the game
     *
     * @param clientHandler that create the lobby
     * @return true if the lobby is created succesfully
     */
    public boolean createLobby(ClientHandler clientHandler) {
        if(!write(clientHandler, "interactionServer", "LOBB-How many players 2 or 3? ")){
            return false;
        }
        try{
            String str = read(clientHandler);
            if(str==null){
                return false;
            }
            numPlayers = Integer.parseInt(str);
        } catch (NumberFormatException e){
            write(clientHandler, "serviceMessage", "LOBB-Invalid input\n");
            createLobby(clientHandler);
        }
        gc.setNumPlayers(numPlayers);
        return true;
    }

    /**
     * sort the list of clienHandlers
     */
    public void sortClientHandlers(){
        boolean change=true;
        ClientHandler ch;
        while(change){
            change=false;
            for(int i=0; i<clientHandlers.size()-1; i++){
                if(clientHandlers.get(i).getAge()>clientHandlers.get(i+1).getAge()){
                    ch=clientHandlers.get(i);
                    clientHandlers.set(i, clientHandlers.get(i+1));
                    clientHandlers.set(i+1, ch);
                    change=true;
                }
            }
        }
    }

    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void setTimeout(boolean t){
        timeout = t;
    }
}
