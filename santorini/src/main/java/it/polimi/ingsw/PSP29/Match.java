package it.polimi.ingsw.PSP29;

import javax.naming.CompositeName;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Match {
    private static int columns = 5;
    private static int rows = 5;
    private Box board[][];
    private ArrayList<Player> players;

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

    public void inizializeBoard(){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++)
                board[i][j] = new Box(i,j);
        }
    }

    public boolean updateMovement(Player p, int id, Box[][] board, Coordinate c){
        if(p.putWorker(id, board, c)) return true;
        else return false;
    }

    public boolean updateBuilding(Coordinate c){
        if(board[c.getX()][c.getY()].upgradeLevel()) return true;
        else return false;
    }

    public boolean removeWorkers(Player p, Box[][] brd){
        Coordinate c1 = p.getWorker(0).getPosition();
        if(c1 != null){
            brd[c1.getX()][c1.getY()].changeState();
            p.getWorker(0).setPosition(null);
        }
        Coordinate c2 = p.getWorker(1).getPosition();
        if(c2 != null){
            brd[c2.getX()][c2.getY()].changeState();
            p.getWorker(1).setPosition(null);
        }
        return true;
    }

    public void printBoard(Box[][] b){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++) {
                b[i][j].printEmpty();
                System.out.println("\t");
            }
            System.out.println("\n");
        }
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }
}