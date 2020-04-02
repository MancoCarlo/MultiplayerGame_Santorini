package it.polimi.ingsw.PSP29.model;

public interface Box_int {
    /**
     * check if box is not occupied by a worker
     * @return true if empty
     */
    public boolean isEmpty();

    /**
     *change state of box from empty to occupied and vice-versa
     */
    public void changeState();

    /**
     * upgrade level of box
     * @return false if tower is already completed
     */
    public boolean upgradeLevel();

    /**
     * print on monitor the state of box: 0 if empty else 1
     */
    public void printEmpty();

    /**
     * print level of box on monitor
     */
    public void printLevel();

}
