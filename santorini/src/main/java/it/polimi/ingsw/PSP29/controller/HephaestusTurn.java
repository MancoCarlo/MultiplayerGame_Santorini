package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;

public class HephaestusTurn extends GodTurn {
    public HephaestusTurn(Turn turn) {
        super(turn);
    }

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    /**
     * allows w to build two times but it has to be in the same box and not a dome
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()!=4 || m.getBoard()[c.getX()][c.getY()].isEmpty()){
            m.updateBuilding(c);
            m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            w.changeBuilt(true);
            if(m.getBoard()[c.getX()][c.getY()].getLevel()<3)//non posso costruire una cupola
                m.updateBuilding(c);
            return true;
        }
        return false;
    }

    /**
     * call move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        return super.move(m, w, c);
    }

    /**
     * call limited_move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        return super.limited_move(m, w, c);
    }

    /**
     * call cantMove() of the superclass
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    @Override
    public boolean cantMove(Match m, Worker w, boolean athena) { return super.cantMove(m, w, athena); }
}
