package it.polimi.ingsw.PSP29;

public class Coordinate {
    public int x;
    public int y;

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

    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    //da fare
    public boolean isNear(Coordinate c){
        return true
    }
}
