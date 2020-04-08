package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;
import java.util.Scanner;

public class AtlasTurn extends GodTurn{

    public AtlasTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean cantMove(Match m, Worker w, boolean athena) {
        return super.cantMove(m, w, athena);
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
                m.getBoard()[c.getX()][c.getY()].setLevelledUp();
            }
        }
        return true;
    }

    /**
     * call move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c) { return super.move(m, w, c); }

    /**
     * call limited_move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c first movement of w
     * @return true if is moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c){ return super.limited_move(m, w, c); }
}
