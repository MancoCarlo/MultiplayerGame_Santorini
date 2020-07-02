package it.polimi.ingsw.PSP29.model;

import java.io.Serializable;

public class Coordinate implements Serializable {
    /**
     * the horizontal coordinate
     */
    private int x;
    /**
     * the vertical coordinate
     */
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

    /**
     * check if two coordinates are adjacent
     * two adjacent coordinates can't be coincident
     * @param a coordinate to analyze
     * @return true when this and a are adjacent
     */
    public boolean isNear(Coordinate a){
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

    /**
     * calculate the next coordinate following the direction of movement
     * @param m the match
     * @param c adjacent coordinate to c in which I want to move
     * @return the next coordinate after c or c if there's no following coordinate or c isn't adjacent
     */
    public Coordinate nextCoordinate(Match m, Coordinate c){
        int a = c.x;
        int b=c.y;
        if(this.isNear(c))
        {
            if(x - c.x == 1)
                a = c.x-1;
            if(x - c.x == -1)
                a = c.x+1;
            if(y - c.y == 1)
                b = c.y -1;
            if(y - c.y == -1)
                b = c.y + 1;
        }
        if(a == -1 || a == m.getRows() || b == -1 || b == m.getColumns())
            return c;
        return new Coordinate(a,b);
    }

    @Override
    public String toString() {
        return x+","+y;
    }
}
