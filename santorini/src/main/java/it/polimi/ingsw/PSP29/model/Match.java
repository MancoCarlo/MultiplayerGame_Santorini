package it.polimi.ingsw.PSP29.model;

import it.polimi.ingsw.PSP29.Controller.GameController;
import it.polimi.ingsw.PSP29.view.Client;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Match implements Serializable {
    private static int columns = 5;
    private static int rows = 5;
    private Box[][] board;
    private ArrayList<Player> players;
    private ArrayList<God> gods;
    private ArrayList<Color> colors;

    public Match() {
        board = new Box[rows][columns];
        players = new ArrayList<Player>();
        gods = new ArrayList<God>();
        colors = new ArrayList<>();
        colors.add(Color.ANSI_BLUE);
        colors.add(Color.ANSI_RED);
        colors.add(Color.ANSI_YELLOW);
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public Box[][] getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String n){
        for(Player p : this.players){
            if(p.getNickname().equals(n)){
                return p;
            }
        }
        return null;
    }

    /**
     * add players to the match
     */
    public void addPlayer(Player player){
            players.add(player);
    }

    /**
     *
     * @param p player to be removed
     */
    public void removePlayer(Player p){
        for(Player player : players){
            if(player.equals(p)){
                removeWorkers(player);
                players.remove(player);
            }
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public ArrayList<God> getGods(){
        return gods;
    }

    public God getGod(int id){
        return gods.get(id);
    }

    /**
     * instantiates each box of the board and assigns the coordinate
     */
    public void inizializeBoard(){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++)
                board[i][j] = new Box(i,j);
        }
    }

    /**
     * reset the levelledUp variable in every box at the end of the turn
     */
    public void resetBoard(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                    board[i][j].setLevelledUp(false);
            }
        }
    }

    /**
     *
     * @param p owner of the worker that make the move
     * @param id identifier's worker
     * @param c destination of the worker
     */
    public void updateMovement(Player p, int id, Coordinate c){
        p.putWorker(id, board, c);
    }

    /**
     * @param c position of the box that must be upgraded
     */
    public void updateBuilding(Coordinate c){
        board[c.getX()][c.getY()].upgradeLevel();
    }

    /**
     *
     * @param p looser player
     */
    public void removeWorkers(Player p){
        Coordinate c1 = p.getWorker(0).getPosition();
        Coordinate c2 = p.getWorker(1).getPosition();
        if(c1 != null) {
            board[c1.getX()][c1.getY()].changeState();
            board[c1.getX()][c1.getY()].setWorkerBox(null);
            p.getWorker(0).setPosition(null);
        }
        if(c2 != null){
            board[c2.getX()][c2.getY()].changeState();
            board[c1.getX()][c1.getY()].setWorkerBox(null);
            p.getWorker(1).setPosition(null);
        }
    }

    /**
     * sort the player by their age
     */
    public void sortPlayers(){
        boolean change=true;
        Player p;
        while(change){
            change=false;
            for(int i=0; i<players.size()-1; i++){
                if(players.get(i).getAge()>players.get(i+1).getAge()){
                    p=players.get(i);
                    players.set(i, players.get(i+1));
                    players.set(i+1, p);
                    change=true;
                }
            }
        }
    }

    /**
     * creates the list of gods
     */
    public void loadGods() {
        gods.add(new God(0, "Apollo", "Il Dio della musica"));
        gods.add(new God(1, "Arthemis", "La Dea della caccia"));
        gods.add(new God(2, "Athena", "La Dea della saggezza"));
        gods.add(new God(3, "Atlas", "Il titano che regge sulle spalle la volta celeste"));
        gods.add(new God(4, "Demeter", "La dea del raccolto"));
        gods.add(new God(5, "Hephaestus", "Il dio delle fucine"));
        gods.add(new God(6, "Minotaur", "Il mostro dalla testa di toro"));
        gods.add(new God(7, "Pan", "Il dio della natura selvaggia"));
        gods.add(new God(8, "Prometheus", "Il titano benefattore dell'umanità"));
        gods.add(new God(9, "Hestia", "La dea del focolare e della casa"));
        gods.add(new God(10, "Poseidon", "Il dio del mare"));
    }

    /**
     * print the board on monitor
     */
    public String printBoard(){
        StringBuilder gameboard = new StringBuilder();
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                gameboard.append(board[i][j].printEmpty(this));
            }
        }
        return gameboard.toString();
    }

    /**
     * @return the string that print the list of gods
     */
    public String printGodlist(){
        String g = "Gods: \n";
        for (int i=0; i<gods.size(); i++){
            g = g + (i+1) + ") " + gods.get(i).getName() + ", " + gods.get(i).getDescription() + "\n";
        }
        return g;
    }

    /**
     * @return the string that print the list of players
     */
    public String printPlayers(){
        String pl= "Players: \n";
        for (Player p : players){
            pl = pl + "- "+ p.getId()+", " + p.getNickname() + ", " + p.getAge() + " years old\n";
        }
        return pl;
    }

    /**
     * control if the username is already taken
     * @param username the username
     * @return true if the username is already taken
     */
    public boolean alreadyIn(String username){
        if(players.size()==0){
            return false;
        }
        else{
            for(Player player : players){
                if(player.getNickname().equals(username)){
                    return true;
                }
            }
        }
        return false;
    }

    public void updatePlayers(ArrayList<ClientHandler> clientHandlers){
        for(ClientHandler clientHandler : clientHandlers){
            if(!clientHandler.getConnected()){
                getPlayer(clientHandler.getName()).setInGame(false);
            }
        }
    }

    public int playersInGame(){
        int count = 0;
        for(Player p : players){
            if(p.getInGame()){
                count++;
            }
        }
        return count;
    }
}
