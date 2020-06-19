package it.polimi.ingsw.PSP29.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Box implements Serializable {
    private Coordinate location;
    private int level;
    private boolean empty;
    private Worker workerBox;
    boolean levelledUp;

    public Box(int x, int y){
        location=new Coordinate(x, y);
        level=0;
        empty=true;
        workerBox = null;
        levelledUp=false;
    }

    public int getLevel() {
        return level;
    }

    public Coordinate getLocation(){
        return location;
    }

    /**
     * check if box is not occupied by a worker
     * @return true if empty
     */
    public boolean isEmpty(){ return empty; }

    /**
     *change state of box from empty to occupied and vice-versa
     */
    public void changeState() {
        if(empty){
            empty=false;
        }
        else{
            empty=true;
        }
    }

    /**
     * upgrade level of box
     */
    public void upgradeLevel(){
        level++;
    }

    public boolean getlevelledUp(){
        return levelledUp;
    }

    public void setLevelledUp(boolean l) {
        levelledUp=l;
    }

    /**
     * calculate level difference between two boxes
     * @param b box given
     * @return number of level difference between this and b
     */
    public int level_diff(Box b){ return this.getLevel()-b.getLevel(); }

    public Worker getWorkerBox() {
        return workerBox;
    }

    /**
     * set the worker to the box
     * @param w the worker
     */
    public void setWorkerBox(Worker w){
        workerBox = w;
    }

    /**
     * print on monitor the state of box: 0 if empty else 1 and the ID of the player in the box
     * @param m match played
     * @return to String of the Box
     */
    public String printEmpty(Match m) {
        String s;
        if(isEmpty()) s="0";
        else{
            s=""+m.getPlayer(workerBox.getIDplayer()).getId();
        }
        return s + level;
    }

}
