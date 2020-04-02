package it.polimi.ingsw.PSP29.model;

public abstract class TurnDecorator implements Turn {

    private final Turn turn;

    protected TurnDecorator(Turn turn){ this.turn=turn; }

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
