package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class AtlasTurn extends GodTurn{

    public AtlasTurn(Turn turn) {
        super(turn);
    }

    /**
     *
     * @param m match played
     * @param w worker that should be moved
     * @param c location of the box that we must check
     * @param athena true if the power of athena is on, else false
     * @return
     */
    public boolean canMoveTo(Match m, Worker w,Coordinate c, boolean athena) {
        return super.canMoveTo(m, w, c, athena);
    }

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that play the turn
     * @return true if p win the game, else false
     */
    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    /**
     * call build() of the superclass while level of the box in c is 4
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w can build in c, else false
     */
    public boolean build(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String answer;
        boolean nopower = super.build(m,w,c);
        if(!nopower) return false;
        System.out.println("Vuoi usare il potere di Atlas? 1) SI 2) NO");
        answer = scanner.nextLine();
        if(answer.equals("1")){
            while(m.getBoard()[c.getX()][c.getY()].getLevel() < 4){
                m.updateBuilding(c);
                m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            }
        }
        return true;
    }

    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn) {
        return super.move(m, ch, server, athenaOn);
    }

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
    }

    @Override
    public String printCoordinates(ArrayList<Coordinate> coordinates) {
        return super.printCoordinates(coordinates);
    }
}
