package it.polimi.ingsw.PSP29.model;

import java.awt.*;
import java.io.Serializable;

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

    /**
     * change the state of the box if a worker builds in the box during his turn
     */
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
     * print on monitor the state of box: 0 if empty else 1
     */
    public String printEmpty() {
        String s= "";
        String s2=""+level;
        if(isEmpty())
            s = "0";
        else{
            if(workerBox.getColor().equals(Color.ANSI_BLUE)){
                s = Color.ANSI_BLUE + workerBox.getIDplayer().substring(0,1) + Color.RESET;
            }
            if(workerBox.getColor().equals(Color.ANSI_RED)){
                s = Color.ANSI_RED + workerBox.getIDplayer().substring(0,1) + Color.RESET;
            }
            if(workerBox.getColor().equals(Color.ANSI_YELLOW)){
                s = Color.ANSI_YELLOW + workerBox.getIDplayer().substring(0,1) + Color.RESET;
            }
        }
        if(level == 0){
            s2 = s2;
        }
        if(level == 1){
            s2 = Color.ANSI_LEVEL1 + s2 + Color.RESET;
        }
        if(level == 2){
            s2 = Color.ANSI_LEVEL2 + s2 + Color.RESET;
        }
        if(level == 3){
            s2 = Color.ANSI_LEVEL3 + s2 + Color.RESET;
        }
        if(level == 4){
            s2 = Color.ANSI_LEVEL4 + s2 + Color.RESET;
        }
        return s + s2;
    }

}
