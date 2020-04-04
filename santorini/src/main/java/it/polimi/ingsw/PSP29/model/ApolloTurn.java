package it.polimi.ingsw.PSP29.model;

import java.util.ArrayList;

public class ApolloTurn extends GodTurn{

    public ApolloTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c){
        return super.build(m, w, c);
    }

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
                w2.setPrev_position(c);
                w2.setPosition(w.getPosition());
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved();
                return true;
            }
        }
    }

    public boolean limited_move(Match m, Worker w, Coordinate c){
        if(m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return super.limited_move(m, w, c);
        }else{
            if(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()==w.getIDplayer() || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c.getX()][c.getY()].getLevel()==4){
                return false;
            }
            else{
                Worker w2 = m.getBoard()[c.getX()][c.getY()].getWorkerBox();
                w2.setPrev_position(c);
                w2.setPosition(w.getPosition());
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved();
                return true;
            }
        }
    }

    public boolean cantMove(Match match,Worker w, boolean athena){
        if(athena){
            for(int i=0; i<match.getRows(); i++){
                for(int j=0; j<match.getColumns(); j++){
                    if(w.getPosition().isNear(match.getBoard()[i][j].getLocation()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[i][j])<1){
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

