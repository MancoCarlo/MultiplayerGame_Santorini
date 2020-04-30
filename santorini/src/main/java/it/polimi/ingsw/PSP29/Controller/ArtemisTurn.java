package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.InputControl.Input;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class ArtemisTurn extends GodTurn{

    public ArtemisTurn(Turn turn) {
        super(turn);
    }

    /**
     * move the worker
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn){
        boolean nopower = super.move(m,ch,server,athenaOn);
        if(!nopower) return false;
        server.write(ch,"serviceMessage", m.printBoard());
        server.write(ch,"interactionServer", "Would you move again?\n1) Yes\n2) No\n");
        String answer = server.read(ch);
        if(answer.equals("1")){
            super.move(m,ch,server,athenaOn);
        }
        return true;
    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param c coordinate that must be checked
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    @Override
    public boolean canMoveTo(Match match,Worker w,Coordinate c, boolean athena){
        if(!athena){
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<=1){
                if(w.getMoved()){
                    if(c.equals(w.getPrev_position())){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                return true;
            }
        } else{
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<1){
                if(w.getMoved()){
                    if(c.equals(w.getPrev_position())){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
