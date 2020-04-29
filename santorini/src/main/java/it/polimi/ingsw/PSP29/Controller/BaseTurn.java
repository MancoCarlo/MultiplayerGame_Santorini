package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;

public class BaseTurn implements Turn {

    /**
     * control if the player wins
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        for(Worker w : p.getWorkers()){
            if(w.getMoved() && w.getBuilt()){
                w.changeMoved(false);
                w.changeBuilt(false);
                if(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel()==3){
                    if(m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel()==3){
                        return true;
                    }
                    else if(!m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel()==2){
                        return true;
                    }
                    else{
                        m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].setLevelledUp(false);
                        return false;
                    }
                }
                m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].setLevelledUp(false);
                return false;
            }
        }
        return false;
    }

    /**
     * let the worker build
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateBuilding(c);
            m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            w.changeBuilt(true);
            return true;
        }
    }

    /**
     * move the worker
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, Worker w, Coordinate c){
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved(true);
            return true;
        }
    }

    /**
     * move the worker if athena has been used
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c){
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved(true);
            return true;
        }
    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param c coordinate that must be checked
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean canMoveTo(Match match,Worker w,Coordinate c, boolean athena){
        if(!athena){
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<=1){
                return true;
            }
        } else{
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<1){
                return true;
            }
        }
        return false;
    }
}
