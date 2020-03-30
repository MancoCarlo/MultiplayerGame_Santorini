package it.polimi.ingsw.PSP29;

import java.util.ArrayList;

public interface Player_int {

    /**
     *
     * put the worker in the coordinate c of the board
     *
     * @param id the worker identifier
     * @param b the gameboard
     * @param c the coordinate in wich the worker had to be put
     * @return true if the worker has been put in the coordinate c, else return false
     */
    public boolean putWorker(int id, Box[][] b, Coordinate c);


    /**
     *
     * let the player draw a god from god list
     *
     * @param g the list of gods
     * @param i the position in wich the god is draw
     */
    public void drawGod(ArrayList<God> g, int i);
}
