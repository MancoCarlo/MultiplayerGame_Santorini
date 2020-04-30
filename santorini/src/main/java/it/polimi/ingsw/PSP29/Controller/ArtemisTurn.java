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
     * move the worker
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn){
        System.out.println("movimento base");
        boolean nopower = super.move(m,ch,server,athenaOn);
        System.out.println("fatto");
        if(!nopower) return false;
        server.write(ch,"serviceMessage", m.printBoard());
        server.write(ch,"interactionServer", "Would you move again?\n1) Yes\n2) No\n");
        String answer = server.read(ch);
        if(answer.equals("1")){
            super.move(m,ch,server,athenaOn);
        }
        return true;
    }

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
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

    @Override
    public String printCoordinates(ArrayList<Coordinate> coordinates) {
        return super.printCoordinates(coordinates);
    }
}
