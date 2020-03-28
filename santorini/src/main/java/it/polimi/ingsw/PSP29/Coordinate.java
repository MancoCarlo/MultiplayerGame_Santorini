package it.polimi.ingsw.PSP29;

public class Coordinate {
    int x;
    int y;

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
}