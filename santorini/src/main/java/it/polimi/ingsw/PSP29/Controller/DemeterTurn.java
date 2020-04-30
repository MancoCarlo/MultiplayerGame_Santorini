package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.InputControl.Input;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class DemeterTurn extends GodTurn {


    public DemeterTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    /**
     * call build() of the superclass and after call another one time build() of the superclass with a new Coordinate
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w can build in c, else false
     */
    public boolean build(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        Coordinate cx = c;
        String answer;
        boolean nopower = super.build(m,w,c);
        if(!nopower) return false;
        if(w.canBuild(m)){
            System.out.println("Vuoi usare il potere di Demeter? 1) SI 2) NO");
            answer = scanner.nextLine();
            if(answer.equals("1")) {
                Coordinate c1;
                do {
                    Input i = new Input();
                    c1 = i.askCoordinate("(Potere Demeter)");
                } while ((c1.getX() == cx.getX() && c1.getY() == cx.getY()) || !super.build(m,w,c1));
                m.getBoard()[c1.getX()][c1.getY()].setLevelledUp(true);
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
