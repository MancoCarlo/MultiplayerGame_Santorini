package it.polimi.ingsw.PSP29.model;

public class PanTurn extends GodTurn {

    public PanTurn(Turn turn) {
        super(turn);
    }

    /**
     * allows the player to win the game if in his turn his worker moved from 2nd level to 3rd level or if he went down 2 or more levels
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        for (Worker w : p.getWorkers()) {
            if (w.getMoved() && w.getBuilt()) {
                w.changeMoved();
                w.changeBuilt();
                if (m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() == 3 && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel() == 2)
                    return true;
                if (m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >= 2)
                    return true;
            }
        }
        return false;
    }

    /**
     *call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w has built in c, else false
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return super.build(m, w, c);
    }

    /**
     *call move() of the superclass
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