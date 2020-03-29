package it.polimi.ingsw.PSP29;

public class Worker {
    private int ID;
    private String IDplayer;
    private Coordinate position = null;

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

    //aggiorna la posizione del worker
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
