package it.polimi.ingsw.PSP29;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    int ID;
    String nickname;
    int age;
    // God card;
    Worker w1;
    Worker w2;

    public Player(int id, String nick, int a) {
        ID = id;
        nickname = nick;
        age = a;
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

    public Worker getW1(){
        return w1;
    }

    public Worker getW2(){
        return w2;
    }
    /*
    public boolean putWorkers(Box[][] B){

    }*/

    public boolean putWorkers(Worker w, Board b) throws IOException {
        int i;
        BufferedInputStream reader = new BufferedInputStream(System.in);
        System.out.println("Coordinate primo worker:");
        i=reader.read();


    }

    public String toString() {
        return "\n\tPlayer{" +
                "nickname='" + nickname + '\'' +
                ", age=" + age + " " + workers.toString() +
                "\n\t}";
    }
}