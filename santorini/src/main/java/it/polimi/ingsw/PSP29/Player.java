package it.polimi.ingsw.PSP29;


import java.util.ArrayList;

public class Player {
    int ID;
    String nickname;
    int age;
    // God card;
    ArrayList<Worker> workers;

    public Player(int id, String nick, int a) {
        ID = id;
        nickname = nick;
        age = a;
        workers = new ArrayList<Worker>();
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
    /*
    public boolean putWorkers(){
        Worker w1=new Worker(1, 1, 1, 1);
        Worker w2=new Worker(2, 1, 2 ,3);
        workers.add(w1);
        workers.add(w2);
        w.get(0).setPosition(c1);
        w.get(1).setPosition(c2);
        return true;
    }*/

    public boolean putWorkers(ArrayList<Worker> w, Coordinate c1, Coordinate c2) {
        Worker w1 = new Worker(1, "");
        Worker w2 = new Worker(2, 1, 2, 3);
        workers.add(w1);
        workers.add(w2);
        w.get(0).setPosition(c1);
        w.get(1).setPosition(c2);
        return true;
    }

    public String toString() {
        return "\n\tPlayer{" +
                "nickname='" + nickname + '\'' +
                ", age=" + age + " " + workers.toString() +
                "\n\t}";
    }
}