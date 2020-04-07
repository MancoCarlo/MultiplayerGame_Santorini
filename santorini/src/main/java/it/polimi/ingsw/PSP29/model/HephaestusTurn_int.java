package it.polimi.ingsw.PSP29.model;

public interface HephaestusTurn_int {
    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    public boolean winCondition(Match m, Player p);

    /**
     * allows w to build two times but it has to be in the same box and not a dome
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    public boolean build(Match m, Worker w, Coordinate c);

    /**
     * call move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c);

    /**
     * call limited_move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c);

    /**
     * call cantMove() of the superclass
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean cantMove(Match m, Worker w, boolean athena);
}
