package it.polimi.ingsw.PSP29.model;

public class Box {
    private Coordinate location;
    private int level;
    private boolean empty;
    private Worker workerBox;

    public Box(int x, int y){
        location=new Coordinate(x, y);
        level=0;
        empty=true;
        workerBox = null;
    }

    public int getLevel() {
        return level;
    }

    public Coordinate getLocation(){
        return location;
    }

    //restituisce lo stato della casella
    public boolean isEmpty(){ return empty; }

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
    public void upgradeLevel(){
        level++;
    }

    public int level_diff(Box b){ return this.getLevel()-b.getLevel(); }

    public Worker getWorkerBox() {
        return workerBox;
    }

    public void setWorkerBox(Worker w){
        workerBox = w;
    }

    /*
    public void printEmpty() {
        if(isEmpty()) System.out.println("0");
        else System.out.println("1");
    }

    public void printLevel() {
        System.out.println(level);
    }
    */
}
