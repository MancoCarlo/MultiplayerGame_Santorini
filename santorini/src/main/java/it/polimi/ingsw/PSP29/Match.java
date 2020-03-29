package it.polimi.ingsw.PSP29;

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

    public int addWorkers(ArrayList<Player> players, Box[][] board){
        for (Player p:players) {
            Worker w1 = new Worker(1, p.getNickname());
            Worker w2 = new Worker(2, p.getNickname());
            try {
                while (!p.putWorkers(w1, w2, board)); //1 solo worker alla volta
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 1;
    }

    public int removeWorkers(Player p, Box[][] brd){
        Coordinate c1 = p.getWorker1().getPosition();
        brd[c1.getX()][c1.getY()].changeState();
        p.w1 = null;
        Coordinate c2 = p.getWorker2().getPosition();
        brd[c2.getX()][c2.getY()].changeState();
        p.w2 = null;
        return 1;
    }

    //metodo provvisorio per caricare tutte le divinità

    public ArrayList<God> loadGods() throws IOException {
        String i , n;
        ArrayList<God> g;
        int id;
        BufferedReader reader = new BufferedReader(new FileReader("ing-sw-2019-Grassi-Manco-Martiri\\santorini\\src\\main\\java\\it\\polimi\\ingsw\\PSP29\\gods.txt"));
        while(true){
            i=reader.readLine();
            if(i==null){
                break;
            }
            id=Integer.parseInt(i);
            n=reader.readLine();
            g.add(new God(id, n));
        }
        return g;
    }

}