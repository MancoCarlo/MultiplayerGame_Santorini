package it.polimi.ingsw.PSP29;

public class Box {
    private Coordinate location;
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

    //restituisce lo stato della casella
    public boolean isEmpty(){
        if(empty){
            return true;
        }
        return false;
    }

    //aggiorna lo stato della casella
    public void changeState() {
        if(empty){
            empty=false;
        }
        else{
            empty=true;
        }
    }

    //aggiorna il livello della casella
    public boolean upgradeLevel(){
        level++;
        return true;
    }

    public void printEmpty() {
        if(isEmpty()) System.out.println("0");
        else System.out.println("1");
    }

    public void printLevel() {
        System.out.println(level);
    }
}
