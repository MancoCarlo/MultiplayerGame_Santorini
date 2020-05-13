package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;

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
            if (w.getMoved()) {
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

}
