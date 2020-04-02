package it.polimi.ingsw.PSP29.model;

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
}
