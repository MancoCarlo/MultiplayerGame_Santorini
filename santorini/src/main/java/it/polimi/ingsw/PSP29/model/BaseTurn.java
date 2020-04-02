package it.polimi.ingsw.PSP29.model;

public class BaseTurn implements Turn {

    @Override
    public boolean winCondition(Match m, Player p) {
        return false;
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return false;
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        return false;
    }

    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        return false;
    }
}
