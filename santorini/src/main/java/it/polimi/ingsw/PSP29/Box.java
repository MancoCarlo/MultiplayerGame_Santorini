package it.polimi.ingsw.PSP29;

public class Box {
    public Coordinate location;
    private int level;
    private boolean empty;

    public Box(int x, int y){
        location=new Coordinate(x, y);
        level=0;
        empty=true;
    }

    public int getLevel() {
        return level;
    }

    public Coordinate getLocation(){
        return location;
    }

    public boolean isEmpty(){
        if(empty){
            return true;
        }
        return false;
    }

    public void changeState() {
        if(empty){
            empty=false;
        }
        else{
            empty=true;
        }
    }

    public void upgradeLevel(){
        level++;
    }
}
