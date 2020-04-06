package it.polimi.ingsw.PSP29.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Match {
    private static int columns = 5;
    private static int rows = 5;
    private Box[][] board;
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

    public Player getPlayer(String n){
        for(Player p : this.players){
            if(p.getNickname().equals(n)){
                return p;
            }
        }
        return null;
    }

    public void addPlayers(){
        Scanner scanner = new Scanner(System.in);
        String name, age;
        int i=0, a;
        while(true){
            System.out.print("Giocatore n." + (i+1) + " inserisci il tuo nome: ");
            name=scanner.nextLine();
            if(name.equals("")){
                break;
            }
            System.out.print(name + ", inserisci la tua età: ");
            age=scanner.nextLine();
            a=Integer.parseInt(age);
            players.add(new Player(i, name, a));
            i++;
        }
    }

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

    public void inizializeBoard(){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++)
                board[i][j] = new Box(i,j);
        }
    }

    public void updateMovement(Player p, int id, Coordinate c){
        p.putWorker(id, board, c);
    }

    public void updateBuilding(Coordinate c){
        board[c.getX()][c.getY()].upgradeLevel();
    }

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

    public void loadGods() throws FileNotFoundException {
        FileReader f = new FileReader("santorini/src/main/java/it/polimi/ingsw/PSP29/model/gods.txt");
        Scanner scanner = new Scanner(f);
        int i;
        String id, n, d;
        while(true){
            id=scanner.nextLine();
            if(id.equals(".")){
                break;
            }
            n=scanner.nextLine();
            d=scanner.nextLine();
            i=Integer.parseInt(id);
            gods.add(new God(i, n, d));
        }
    }

    public void printBoard(Box[][] b){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns;j++) {
                b[i][j].printEmpty();
                System.out.print("\t");
            }
            System.out.println("");
        }
    }

    public void printGodlist(){
        System.out.println("lista:");
        for (God g : gods){
            System.out.println("- " + g.toString());
        }
    }

    public void printPlayers(){
        System.out.println("lista giocatori:");
        for (Player p : players){
            System.out.println("- " + p.toString());
        }
    }
}
