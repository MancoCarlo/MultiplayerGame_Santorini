package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import java.util.Scanner;

public class HephaestusTurn extends GodTurn {
    public HephaestusTurn(Turn turn) {
        super(turn);
    }

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    /**
     * allows w to build two times but it has to be in the same box and not a dome
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        boolean nopower = super.build(m,w,c);
        if(!nopower) return false;
        Scanner scanner = new Scanner(System.in);
        String answer;
        if(m.getBoard()[c.getX()][c.getY()].getLevel()<3) { //non posso costruire una cupola
            System.out.println("Vuoi usare il potere di Hephaestus? 1) SI 2) NO\n");
            answer = scanner.nextLine();
            if (answer.equals("1")) {
                m.updateBuilding(c);
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
    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        return super.move(m, w, c);
    }

    /**
     * call limited_move() of the superclass
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        return super.limited_move(m, w, c);
    }

    /**
     * call cantMove() of the superclass
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    @Override
    public boolean cantMove(Match m, Worker w, boolean athena) { return super.cantMove(m, w, athena); }
}
