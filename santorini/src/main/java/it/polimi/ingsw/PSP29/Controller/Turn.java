package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

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
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn);

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
     * @param c coordinate that must be checked
     * @return true if w can't move to another location, else false
     */
    public boolean canMoveTo(Match m,Worker w, Coordinate c, boolean athena);

    /**
     * create an arrayList with all the coordinates in wich the worker can move
     * @param match match played
     * @param ch owner of turn
     * @param id the worker id
     * @param athenaOn true if athena is on
     * @return the list
     */
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn);

    /**
     * print the list of valids coordinate
     * @param coordinates
     * @return the string that print the list
     */
    public String printCoordinates(ArrayList<Coordinate> coordinates);

}
