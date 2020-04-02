package it.polimi.ingsw.PSP29.model;

public interface Coordinate_int {

    /**
     * check if two coordinates are adjacent
     * two adjacent coordinates can't be coincident
     * @param a coordinate to analyze
     * @return true when this and a are adjacent
     */
    public boolean isNear(Coordinate a);
}
