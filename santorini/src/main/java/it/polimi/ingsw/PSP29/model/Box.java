package it.polimi.ingsw.PSP29.model;

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
     * change the state of the box if a worker build in the box
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
    public void printEmpty() {
        if(isEmpty()) System.out.print("0" + level);
        else{
            System.out.print(workerBox.getIDplayer().charAt(0));
            System.out.print(level);
        }
    }

    /**
     * print level of box on monitor
     */
    public void printLevel() {
        System.out.println(level);
    }
}
