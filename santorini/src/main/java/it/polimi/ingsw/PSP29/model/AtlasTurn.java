package it.polimi.ingsw.PSP29.model;

import java.util.Scanner;

public class AtlasTurn extends GodTurn{

    public AtlasTurn(Turn turn) {
        super(turn);
    }

    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    public boolean build(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String answer;
        boolean nopower = super.build(m,w,c);
        if(!nopower) return false;
        System.out.println("Vuoi usare il potere di Atlas? 1) SI 2) NO\n");
        answer = scanner.nextLine();
        if(answer == "1"){
            while(m.getBoard()[c.getX()][c.getY()].getLevel() < 4){
                m.updateBuilding(c);
            }
        }
        return true;
    }

    public boolean move(Match m, Worker w, Coordinate c) { return super.move(m, w, c); }

    public boolean limited_move(Match m, Worker w, Coordinate c){ return super.limited_move(m, w, c); }
}
