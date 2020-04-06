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

}
