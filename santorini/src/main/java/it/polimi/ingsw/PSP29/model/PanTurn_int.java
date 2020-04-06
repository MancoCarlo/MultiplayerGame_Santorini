package it.polimi.ingsw.PSP29.model;

public interface PanTurn_int {
    /**
     * allows the player to win the game if in his turn his worker moved from 2nd level to 3rd level or if he went down 2 or more levels
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
     */
    public boolean winCondition(Match m, Player p);

    /**
     *call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w has built in c, else false
     */
    public boolean build(Match m, Worker w, Coordinate c);

    /**
     *call move() of the superclass
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
