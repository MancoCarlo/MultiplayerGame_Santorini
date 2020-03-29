package it.polimi.ingsw.PSP29;

import java.util.ArrayList;

public class Match {
    private static int columns = 5;
    private static int rows = 5;
    private Box board[][];
    private ArrayList<Player> players;
    //provvisorio
    private ArrayList<God> gods;

    public Match() {
        board = new Box[rows][columns];
        players = new ArrayList<Player>();
    }

    public Box[][] getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean updateMovement(Player p, int id, Box[][] board, Coordinate c){
        if(p.putWorker(id, board, c)) return true;
        else return false;
    }

    public boolean updateBuilding(Player p, int id, Box[][] board, Coordinate c){
        if(c.isNear(p.getWorker(id).getPosition()) && board[c.getX()][c.getY()].isEmpty()){
            if(board[c.getX()][c.getY()].upgradeLevel()) return true;
            else return false;
        }else{
            return false;
        }
    }

    public boolean removeWorkers(Player p, Box[][] brd){
        Coordinate c1 = p.getWorker(0).getPosition();
        brd[c1.getX()][c1.getY()].changeState();
        p.getWorker(0).setPosition(null);
        Coordinate c2 = p.getWorker(1).getPosition();
        brd[c2.getX()][c2.getY()].changeState();
        p.getWorker(1).setPosition(null);
        return true;
    }

}