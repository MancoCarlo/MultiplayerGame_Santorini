package it.polimi.ingsw.PSP29.model;

public class Worker {
    private int ID;
    private String IDplayer;
    private Coordinate position = null;
    private Coordinate prev_position = null;
    private boolean moved;
    private boolean built;

    public Worker(int id, String nickP){
        ID=id;
        IDplayer=nickP;
        position=null;
        prev_position=null;
        moved = false;
        built = false;
    }

    public int getID() {
        return ID;
    }

    public String getIDplayer() {
        return IDplayer;
    }

    public Coordinate getPosition(){
        return position;
    }

    public Coordinate getPrev_position() { return prev_position; }

    public boolean getMoved(){ return moved; }

    public void changeMoved(){
        moved= !moved;
    }

    public void changeBuilt() {
        built= !built;
    }

    public boolean getBuilt(){ return built; }


    public void setPosition(Coordinate c){
        position=c;
    }

    public void setPrev_position(Coordinate c) { prev_position=c; }

}
