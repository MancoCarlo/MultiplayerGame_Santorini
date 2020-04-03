package it.polimi.ingsw.PSP29.model;

import java.util.Scanner;

public interface ArtemisTurn_int {

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that play the turn
     * @return true if p win the game, else false
     */
    public boolean winCondition(Match m, Player p);

    /**
     * call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w can build in c, else false
     */
    public boolean build(Match m, Worker w, Coordinate c);

    /**
     * w can make 2 movement, but can't return in his initial position
     * @param m match played
     * @param w worker that must be moved
     * @param c first movement of w
     * @return true if w can make 2 movement, else false
     */
    public boolean move(Match m, Worker w, Coordinate c);


    /**
     *  w can make 2 movement, but can't return in his initial position and all the box where w pass have the same level
     * @param m match played
     * @param w worker that must be moved
     * @param c first movement of w
     * @return true if w can make 2 movement, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c);
}
