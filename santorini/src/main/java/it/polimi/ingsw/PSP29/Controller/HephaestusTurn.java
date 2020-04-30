package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class HephaestusTurn extends GodTurn {
    public HephaestusTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p) { return super.winCondition(m, p); }

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

    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn) {
        return super.move(m, ch, server, athenaOn);
    }

    @Override
    public boolean canMoveTo(Match m,Worker w,Coordinate c, boolean athena){ return super.canMoveTo(m,w,c,athena);
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
