package it.polimi.ingsw.PSP29.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String nickname;
    private int age;
    private int id;
    God card;
    ArrayList<Worker> workers;
    private boolean inGame = false;

    public Player(String nick, int a, int id) {
        nickname = nick;
        age = a;
        this.id = id;
        workers = new ArrayList<Worker>();
        for(int i = 0;i<2;i++){
            workers.add(new Worker(i,nick));
        }
    }

    public int getId() { return id; }

    public boolean getInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getAge() {
        return age;
    }

    public String getNickname() {
        return nickname;
    }

    public God getCard() { return card; }

    public Worker getWorker(int id){
        return workers.get(id);
    }

    public ArrayList<Worker> getWorkers() { return workers; }

    /**
     *
     * put the worker in the coordinate c of the board
     *
     * @param id the worker identifier
     * @param b the gameboard
     * @param c the coordinate in wich the worker had to be put
     */
    public void putWorker(int id, Box[][] b, Coordinate c) {
        if (workers.get(id).getPosition() == null) {
            b[c.getX()][c.getY()].changeState();
            b[c.getX()][c.getY()].setWorkerBox(workers.get(id));
            workers.get(id).setPosition(c);
            workers.get(id).setPrev_position(c);
        } else {
            workers.get(id).setPrev_position(workers.get(id).getPosition());
            b[workers.get(id).getPrev_position().getX()][workers.get(id).getPrev_position().getY()].changeState();
            b[workers.get(id).getPrev_position().getX()][workers.get(id).getPrev_position().getY()].setWorkerBox(null);
            b[c.getX()][c.getY()].changeState();
            b[c.getX()][c.getY()].setWorkerBox(workers.get(id));
            workers.get(id).setPosition(c);
        }
    }

    /**
     * draw a god from the list
     * @param g the list of gods
     * @param i the position of the card in the list
     */
    public void setCard(ArrayList<God> g, int i){
        card=g.get(i);
    }

    /**
     * return a string that contains player's worker and them position
     * @return
     */
    public String printWorkers(){
        String s="Workers:\n";
        for(int i=0; i<workers.size(); i++){
            s = s + (i+1) + ") " + workers.get(i).toString();
        }
        return s;
    }

    @Override
    public String toString() {
        return "Player{" +
                " nickname='" + nickname + '\'' +
                ", age=" + age +
                ", card=" + card +
                '}';
    }
}