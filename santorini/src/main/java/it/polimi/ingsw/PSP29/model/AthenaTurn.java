package it.polimi.ingsw.PSP29.model;

import java.util.ArrayList;

public class AthenaTurn extends GodTurn{

    public AthenaTurn(Turn turn) {
        super(turn);
    }

    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    public boolean build(Match m, Worker w, Coordinate c){
        return super.build(m, w, c);
    }

    public boolean move(Match m, Worker w, Coordinate c) { return super.move(m, w, c); }
}