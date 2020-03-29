package it.polimi.ingsw.PSP29;

public interface Match_int {
    /**
     * instantiates each box of the board and assigns the coordinate
     */
    public void inizializeBoard();

    /**
     *
     * @param p owner of the worker that make the move
     * @param id identifier's worker
     * @param board game chessboard
     * @param c destination of the worker
     * @return true if the worker is moved in position c
     */
    public boolean updateMovement(Player p, int id, Box[][] board, Coordinate c);

    /**
     *
     * @param c position of the box that must be upgraded
     * @return true if the box can be upgraded
     */
    public boolean updateBuilding(Coordinate c);

    /**
     *
     * @param p looser player
     * @param brd game board
     * @return true when the player's workers are removed
     */
    public boolean removeWorkers(Player p, Box[][] brd);

    /**
     * print the board on monitor
     * @param b game board
     */
    public void printBoard(Box[][] b);
}
