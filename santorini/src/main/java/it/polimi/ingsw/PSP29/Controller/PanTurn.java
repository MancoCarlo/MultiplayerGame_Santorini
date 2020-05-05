package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class PanTurn extends GodTurn {

    public PanTurn(Turn turn) {
        super(turn);
    }

    /**
     * allows the player to win the game if in his turn his worker moved from 2nd level to 3rd level or if he went down 2 or more levels
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        for (Worker w : p.getWorkers()) {
            if (w.getMoved() && w.getBuilt()) {
                w.changeMoved(false);
                w.changeBuilt(false);
                if(m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel()==3 && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel()==3)
                    return true;
                if (!m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() == 3 && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel() == 2)
                    return true;
                if(m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >= 3)
                    return true;
                if (!m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >= 2)
                    return true;
            }
        }
        return false;
    }


    @Override
    public boolean build(Match m, ClientHandler ch, Server server) {
        return super.build(m, ch, server);
    }

    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn) {
        return super.move(m, ch, server, athenaOn);
    }

    @Override
    public boolean canBuildIn(Match match,Worker w,Coordinate c){
        return super.canBuildIn(match, w, c);
    }

    @Override
    public boolean canMoveTo(Match m,Worker w,Coordinate c, boolean athena){ return super.canMoveTo(m,w,c,athena);
    }

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
    }

    @Override
    public ArrayList<Coordinate> whereCanBuild(Match match, ClientHandler ch, int id) {
        return super.whereCanBuild(match, ch, id);
    }

    @Override
    public String printCoordinates(ArrayList<Coordinate> coordinates) {
        return super.printCoordinates(coordinates);
    }
}
