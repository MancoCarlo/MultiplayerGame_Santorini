package it.polimi.ingsw.PSP29.model;

import java.io.Serializable;

public class Worker implements Serializable {
    private int ID;
    private String IDplayer;
    private Coordinate position = null;
    private Coordinate prev_position = null;
    private boolean moved;
    private boolean built;

    public Worker(int id, String nickP){
        ID=id;
        IDplayer=nickP;
        position=null;
        prev_position=null;
        moved = false;
        built = false;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setIDplayer(String IDplayer) {
        this.IDplayer = IDplayer;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void setBuilt(boolean built) {
        this.built = built;
    }

    public int getID() {
        return ID;
    }

    public String getIDplayer() {
        return IDplayer;
    }

    public Coordinate getPosition(){
        return position;
    }

    public Coordinate getPrev_position() { return prev_position; }

    public boolean getMoved(){ return moved; }

    /**
     * change if the worker has been moved in this turn
     */
    public void changeMoved(boolean m){ moved = m; }

    /**
     * change if the worker has built in this turn
     */
    public void changeBuilt(boolean b) { built=b;    }

    public boolean getBuilt(){ return built; }

    /**
     *  set the position of the worker in the coordinate c
     * @param c the coordinate
     */
    public void setPosition(Coordinate c){
        position=c;
    }

    /**
     * set the previous position of the worker in the coordinate c
     * @param c the coordinate
     */
    public void setPrev_position(Coordinate c) { prev_position=c; }

    /**
     * checks if the worker can upgrade his level before doing a move
     * @param match the match in which the worker is in
     * @return true if the worker can upgrade his level
     */
    public boolean canLevelUp (Match match) {
        for(int i=-1; i<=1; i++)
        {
            if(this.getPosition().getX() + i==-1 || this.getPosition().getX() + i == match.getRows()) continue;
            for(int j=-1; j<=1; j++)
            {
                if(this.getPosition().getY()+j==-1 || this.getPosition().getY() + j ==match.getColumns()) continue;

                if(match.getBoard()[this.getPosition().getX()][this.getPosition().getY()].level_diff(match.getBoard()[this.getPosition().getX()+i][this.getPosition().getY()+j])==-1 && match.getBoard()[this.getPosition().getX()][this.getPosition().getY()].getLevel() <3)
                    return true;

            }
        }
        return false;

    }

    /**
     * control if the worker can build
     * @param m the match
     * @return true if the worker can build
     */
    public boolean canBuild(Match m){
        for(int i=0; i<m.getRows(); i++){
            for(int j=0; j<m.getColumns(); j++){
                if(m.getBoard()[i][j].getLocation().isNear(position) && m.getBoard()[i][j].isEmpty() && m.getBoard()[i][j].getLevel()!=4){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Worker{" + ID +", "+
                " position=" + position + " }\n";
    }
}
