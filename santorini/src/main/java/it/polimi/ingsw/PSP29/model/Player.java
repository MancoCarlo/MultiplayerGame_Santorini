package it.polimi.ingsw.PSP29.model;

import it.polimi.ingsw.PSP29.controller.NotValidInputException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Serializable {
    private String nickname;
    private int age;
    God card;
    ArrayList<Worker> workers;

    public Player(String nick, int a) {
        nickname = nick;
        age = a;
        workers = new ArrayList<Worker>();
        for(int i = 0;i<2;i++){
            workers.add(new Worker(i,nick));
        }
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
     * @return true if the worker has been put in the coordinate c, else return false
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
     * let the player draw a card from the list of gods
     * @param gods the list of gods
     */

    /**
     * draw a god from the list
     * @param g the list of gods
     * @param i the position of the card in the list
     */
    public void setCard(ArrayList<God> g, int i){
        card=g.get(i);
    }

    public void printWorkers(){
        System.out.println("lista worker:");
        for (Worker w : workers){
            System.out.println("- " + w.toString());
        }
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