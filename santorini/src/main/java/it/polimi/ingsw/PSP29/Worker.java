package it.polimi.ingsw.PSP29;

public class Worker {
    int ID;
    String IDplayer;
    Coordinate position;

    public Worker(int id, String nickP){
        ID=id;
        IDplayer=nickP;
    }

    public Coordinate getPosition(){
        return position;
    }


    public int getID() {
        return ID;
    }

    public String getIDplayer() {
        return IDplayer;
    }

    public void setPosition(Coordinate c){
        position=c;
    }

    public String toString() {
        return "\n\t\tWorker{" +
                "ID=" + ID +
                ", IDplayer=" + IDplayer +
                ", position=" + position.toString() +
                '}';
    }
}
