package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;

public interface Turn {

    /**
     * let the worker build
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    public boolean build(Match m, Worker w, Coordinate c);

    /**
     * move the worker
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c);

    /**
     * move the worker if athena has been used
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c);

    /**
     * control if the player win
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    public boolean winCondition(Match m, Player p);

    /**
     * control if the worker can move
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean cantMove(Match m,Worker w, boolean athena);

}
