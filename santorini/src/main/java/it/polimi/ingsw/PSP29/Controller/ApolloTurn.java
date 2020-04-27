package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;

public class ApolloTurn extends GodTurn{

    public ApolloTurn(Turn turn) {
        super(turn);
    }

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that play the turn
     * @return true if p win the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    /**
     * call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w can build in c, else false
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c){
        return super.build(m, w, c);
    }

    /**
     * move w in c, if !isEmpty() and occuped by enemy worker then w and the worker in c swap their position
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if w can be moved in c, else false
     */
    @Override
    public boolean move(Match m, Worker w, Coordinate c){
        if(m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return super.move(m, w, c);
        }else{
            if(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()==w.getIDplayer() || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c.getX()][c.getY()].getLevel()==4){
                return false;
            }
            else{
                Worker w2 = m.getBoard()[c.getX()][c.getY()].getWorkerBox();
                w2.setPosition(null);
                w2.setPrev_position(null);
                m.getBoard()[c.getX()][c.getY()].setWorkerBox(null);
                m.getBoard()[c.getX()][c.getY()].changeState();
                Coordinate cx = w.getPosition();
                super.move(m,w,c);
                m.getPlayer(w2.getIDplayer()).putWorker(w2.getID(), m.getBoard(), cx);
                w2.setPrev_position(c);
                return true;
            }
        }
    }

    /**
     * move w in c, if !isEmpty() and occuped by enemy worker then w and the worker in c swap their position. oldposition of w and new position of w have the same level
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if w can be moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c){
        if(m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return super.limited_move(m, w, c);
        }else{
            if(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()==w.getIDplayer() || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c.getX()][c.getY()].getLevel()==4){
                return false;
            }
            else{
                Worker w2 = m.getBoard()[c.getX()][c.getY()].getWorkerBox();
                w2.setPosition(null);
                w2.setPrev_position(null);
                m.getBoard()[c.getX()][c.getY()].setWorkerBox(null);
                m.getBoard()[c.getX()][c.getY()].changeState();
                Coordinate cx = w.getPosition();
                super.limited_move(m,w,c);
                m.getPlayer(w2.getIDplayer()).putWorker(w2.getID(), m.getBoard(), cx);
                w2.setPrev_position(c);
                return true;
            }
        }
    }

    /**
     *
     * @param match match played
     * @param w worker that must be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean cantMove(Match match,Worker w, boolean athena){
        if(athena){
            for(int i=0; i<match.getRows(); i++){
                for(int j=0; j<match.getColumns(); j++){
                    if(w.getPosition().isNear(match.getBoard()[i][j].getLocation()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[i][j])==0){
                        return false;
                    }
                }
            }
        }
        else{
            for(int i=0; i<match.getRows(); i++){
                for(int j=0; j<match.getColumns(); j++){
                    if(w.getPosition().isNear(match.getBoard()[i][j].getLocation()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[i][j])<2){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

