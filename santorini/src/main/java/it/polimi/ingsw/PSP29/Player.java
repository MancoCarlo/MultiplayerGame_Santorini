package it.polimi.ingsw.PSP29;

import java.util.ArrayList;

public class Player {
    private int ID;
    private String nickname;
    private int age;
    God card;
    ArrayList<Worker> workers=new ArrayList<Worker>();

    public Player(int id, String nick, int a) {
        ID = id;
        nickname = nick;
        age = a;
        for(int i=0; i<2; i++){
            workers.add(new Worker(i, nickname));
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
        if(workers.get(id).getPosition()==null && b[c.getX()][c.getY()].isEmpty()){
            b[c.getX()][c.getY()].changeState();
            workers.get(id).setPosition(c);
            return true;
        }
        else if(workers.get(id).getPosition().isNear(c) && b[c.getX()][c.getY()].isEmpty()){
            b[c.getX()][c.getY()].changeState();
            workers.get(id).setPosition(c);
            return true;
        }
        return false;
    }
    
    public String toString() {
        return "Player{" +
                "ID=" + ID +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", card=" + card +
                ", workers=" + workers +
                '}';
    }

    //pesca una divinità tra quelle presenti
    public void drawGod(ArrayList<God> g, int i){
        card=g.get(i);
    }
}