package it.polimi.ingsw.PSP29.model;

public interface Coordinate_int {

    /**
     * check if two coordinates are adjacent
     * two adjacent coordinates can't be coincident
     * @param a coordinate to analyze
     * @return true when this and a are adjacent
     */
    public boolean isNear(Coordinate a);

    /**
     * calculate the next coordinate following the direction of movement
     * @param c coordinate in which I want to move
     * @return the next coordinate after c or c if there's no following coordinate
     */
    public Coordinate nextCoordinate(Coordinate c);
}
