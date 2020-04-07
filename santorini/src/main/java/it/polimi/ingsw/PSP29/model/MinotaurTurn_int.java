package it.polimi.ingsw.PSP29.model;

public interface MinotaurTurn_int {
    /**
     *call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
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
     * allows a player to move his worker in an adjacent box already occupied by a worker of another player and change that worker's position in the following box
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w occupied by a worker of another player
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c);

    /**
     * allows a player to move his worker without upgrading his level in an adjacent box already occupied by a worker of another player and change that worker's position in the following box
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w occupied by a worker of another player
     * @return true if is moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c);

    /**
     * checks if a player can't move his worker using Minotaur power
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean cantMove(Match m, Worker w, boolean athena);
}
