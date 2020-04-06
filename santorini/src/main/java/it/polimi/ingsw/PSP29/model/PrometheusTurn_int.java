package it.polimi.ingsw.PSP29.model;

public interface PrometheusTurn_int {
    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    public boolean winCondition(Match m, Player p);

    /**
     * call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w has built in c, else false
     */
    public boolean build(Match m, Worker w, Coordinate c);

    /**
     * allows a player to build before and after moving his worker if in this turn his worker can't level up
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c);

    /**
     * allows a player to build before and after moving his worker without letting him level up
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c);

    /**
     * checks if a player can't move his worker using Prometheus power
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean cantMove(Match m,Worker w, boolean athena);

}
