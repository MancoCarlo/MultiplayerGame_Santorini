package it.polimi.ingsw.PSP29.model;

public interface Turn {


    public boolean build(Match m, Worker w, Coordinate c);


    public boolean move(Match m, Worker w, Coordinate c);


    public boolean limited_move(Match m, Worker w, Coordinate c);


    public boolean winCondition(Match m, Player p);


}
