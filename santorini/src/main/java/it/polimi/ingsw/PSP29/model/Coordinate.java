package it.polimi.ingsw.PSP29.model;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int a, int b){
        x=a;
        y=b;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean isNear(Coordinate a){ //due coordinate coincidenti non sono vicine
        if(!this.equals(a) && ((a.x-x<=1) && (a.x-x>=-1)) && (a.y-y<=1)&&(a.y-y>=-1))
            return true;
        else
            return false;
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Coordinate))
            return false;
        Coordinate c = (Coordinate) o;
        return x == c.x && y == c.y;
    }

}
