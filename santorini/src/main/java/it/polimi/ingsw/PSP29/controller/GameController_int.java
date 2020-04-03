package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;

public interface GameController_int {

    /**
     *
     * used for the execution of the game
     *
     * @throws NotValidInputException
     */
    public void gameExe() throws NotValidInputException;

    /**
     *
     * @param player the player that plays the turn
     * @return true if the player lost
     */
    public boolean loseControl(Player player);

    /**
     *
     * used to create a new turn
     *
     * @param p the player that plays the turn
     * @return true if player win
     * @throws NotValidInputException
     */
    public boolean newTurn(Player p) throws NotValidInputException;

    /**
     *
     * used for the execution of the turn
     *
     * @param p the player that plays the turn
     * @param turn the turn, can be BaseTurn or one of the gods' turn
     * @return the result of winCondition
     */
    public boolean turnExe(Player p, Turn turn);

    /**
     *
     * read the Coordinate from the player input
     *
     * @param c the var in wich i put the coordinate where the worker moves
     * @param str used for output line
     */
    public void askCoordinate(Coordinate c, String str);

    /**
     *
     * used to ask to the player the x or the y of the coordinate
     *
     * @param c the var in wich i put the x or the y of the coordinate where the worker moves
     */
    public void ask_x_y(int c);

    /**
     *
     * used to ask to the player wich of his workers he wants to move
     *
     * @param id the var where i put the worker's id
     * @param p the player that plays the turn
     */
    public void askWorker(int id, Player p);

    /**
     * used to ask if the player wants to use his god
     */
    public void askGod();
}
