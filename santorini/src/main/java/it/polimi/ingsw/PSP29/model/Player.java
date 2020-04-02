package it.polimi.ingsw.PSP29;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int ID;
    private String nickname;
    private int age;
    //God card;
    ArrayList<Worker> workers;

    public Player(int id, String nick, int a) {
        ID = id;
        nickname = nick;
        age = a;
        workers = new ArrayList<Worker>();
        for(int i = 0;i<2;i++){
            workers.add(new Worker(i,nick));
        }
    }

    public int getID() {
        return ID;
    }

    public int getAge() {
        return age;
    }

    public String getNickname() {
        return nickname;
    }

    public Worker getWorker(int id){
        return workers.get(id);
    }

    //modifica la posizione del worker e cambia lo stato della casella in cui si trova
    public boolean putWorker(int id, Box[][] b, Coordinate c) {
        if (workers.get(id).getPosition() == null){
            if(b[c.getX()][c.getY()].isEmpty()){
                b[c.getX()][c.getY()].changeState();
                workers.get(id).setPosition(c);
                return true;
            }else return false;
        }else if(workers.get(id).getPosition().isNear(c) && b[c.getX()][c.getY()].isEmpty()) {
            b[c.getX()][c.getY()].changeState();
            workers.get(id).setPosition(c);
            return true;
        }else return false;
    }

}
