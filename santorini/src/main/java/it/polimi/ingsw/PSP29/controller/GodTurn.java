package it.polimi.ingsw.PSP29.model;

public class GodTurn extends TurnDecorator{

    public GodTurn(Turn turn) { super(turn);}

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return super.build(m, w, c);
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c){ return super.move(m, w, c); }

    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c){ return super.limited_move(m, w, c); }

    public boolean cantMove(Match m,Worker w, boolean athena){ return super.cantMove(m,w,athena);
    }
}
