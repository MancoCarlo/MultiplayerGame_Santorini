package it.polimi.ingsw.PSP29;

public class Player {
    private int ID;
    private String nickname;
    private int age;
    // God card;
    public Worker w1;
    public Worker w2;

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

    public Worker getWorker1(){
        return w1;
    }

    public Worker getWorker2(){
        return w2;
    }

    public void putWorkers(Worker w, Box[][] b, Coordinate c) {
        b[c.x][c.y].changeState();
        w.setPosition(c);
    }

    public String toString() {
        return "Player{" +
                "ID=" + ID +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", w1=" + w1 +
                ", w2=" + w2 +
                '}';
    }
}