package it.polimi.ingsw.PSP29.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Match {
    private static int columns = 5;
    private static int rows = 5;
    private Box board[][];
    private ArrayList<Player> players;
    private ArrayList<God> gods;

    public Match() {
        board = new Box[rows][columns];
        players = new ArrayList<Player>();
        gods = new ArrayList<God>();
    }

    public Box[][] getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }

    public void inizializeBoard(){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++)
                board[i][j] = new Box(i,j);
        }
    }

    public void updateMovement(Player p, int id, Box[][] board, Coordinate c){
        p.putWorker(id, board, c);
    }

    public void updateBuilding(Coordinate c){
        board[c.getX()][c.getY()].upgradeLevel();
    }

    public void removeWorkers(Player p, Box[][] brd){
        Coordinate c1 = p.getWorker(0).getPosition();
        Coordinate c2 = p.getWorker(1).getPosition();
        if(c1 != null) {
            brd[c1.getX()][c1.getY()].changeState();
            p.getWorker(0).setPosition(null);
        }
        if(c2 != null){
            brd[c2.getX()][c2.getY()].changeState();
            p.getWorker(1).setPosition(null);
        }
    }

    public void loadGods() throws FileNotFoundException {
        FileReader f = new FileReader("src/main/java/it/polimi/ingsw/PSP29/gods.txt");
        Scanner scanner = new Scanner(f);
        int i;
        String id, n, d;
        while(true){
            id=scanner.nextLine();
            if(id==null){
                break;
            }
            n=scanner.nextLine();
            d=scanner.nextLine();
            i=Integer.parseInt(id);
            gods.add(new God(i, n, d));
        }
    }
/*
    public void printBoard(Box[][] b){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++) {
                b[i][j].printEmpty();
                System.out.println("\t");
            }
            System.out.println("\n");
        }
    }
*/
}
