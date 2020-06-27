package it.polimi.ingsw.PSP29.model;

import it.polimi.ingsw.PSP29.virtualView.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;

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

    public Box[][] getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * get a player from his name
     * @param n Username of the player that must be searched
     * @return null if there isn't n in players list, else return the Player
     */
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
     * @param player the player that must be added to players list
     */
    public void addPlayer(Player player){
            players.add(player);
    }

    /**
     * remove a player from the players list
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
     * update the position of one worker in game
     * @param p owner of the worker that make the move
     * @param id identifier's worker
     * @param c destination of the worker
     */
    public void updateMovement(Player p, int id, Coordinate c){
        p.putWorker(id, board, c);
    }

    /**
     * Upgrade the level of one box in the board
     * @param c position of the box that must be upgraded
     */
    public void updateBuilding(Coordinate c){
        board[c.getX()][c.getY()].upgradeLevel();
    }

    /**
     * remove the workers of the looser player
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
        gods.add(new God(0, "Apollo", "God of Music"));
        gods.add(new God(1, "Arthemis", "Goddess of the Hunt"));
        gods.add(new God(2, "Athena", "Goddess of Wisdom"));
        gods.add(new God(3, "Atlas", "Titan Shouldering the Heavens"));
        gods.add(new God(4, "Demeter", "Goddess of the Harvest"));
        gods.add(new God(5, "Hephaestus", "God of Blacksmiths"));
        gods.add(new God(6, "Minotaur", "Bull-headed Monster"));
        gods.add(new God(7, "Pan", "God of the Wild"));
        gods.add(new God(8, "Prometheus", "Titan Benefactor of Mankind"));
        gods.add(new God(9, "Hestia", "Goddess of Heart h and Home"));
        gods.add(new God(10, "Poseidon", "God of the Sea"));
        gods.add(new God(11, "Triton", "God of the Waves"));
        gods.add(new God(12, "Charon", "Ferryman to the Underworld"));
        gods.add(new God(13, "Zeus", "God of the sky"));
    }

    /**
     * print the board on monitor
     * @return toString of Board
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
     * return a string that contains the list of gods
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
     * return a string that contains the list of the players
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

    /**
     * remove a player from the game if disconnected
     * @param clientHandlers list of clients
     */
    public void updatePlayers(ArrayList<ClientHandler> clientHandlers){
        for(ClientHandler clientHandler : clientHandlers){
            if(!clientHandler.getConnected()){
                getPlayer(clientHandler.getName()).setInGame(false);
            }
        }
    }

    /**
     * return the number of players in game
     * @return count of the number of players in game
     */
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
