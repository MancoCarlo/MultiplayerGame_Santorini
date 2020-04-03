package it.polimi.ingsw.PSP29.model;

public interface Worker_int {

    /**
     *  set the position of the worker in the coordinate c
     * @param c the coordinate in wich the worker had to be put
     */
    public void setPosition(Coordinate c);

    /**
     *
     * @param match the match in wich the worker is in
     * @return true if the worker can't move
     */
    public boolean cantMove(Match match);
}
