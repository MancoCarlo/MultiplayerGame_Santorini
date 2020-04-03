package it.polimi.ingsw.PSP29.model;

public interface ApolloTurn_int {
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
     * move w in c, if !isEmpty() and occuped by enemy worker then w and the worker in c swap their position
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if w can be moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c);

    /**
     * move w in c, if !isEmpty() and occuped by enemy worker then w and the worker in c swap their position. oldposition of w and new position of w have the same level
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if w can be moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c);
}
