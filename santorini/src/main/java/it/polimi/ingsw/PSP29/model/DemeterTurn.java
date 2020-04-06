package it.polimi.ingsw.PSP29.model;

import java.util.Scanner;

public class DemeterTurn extends GodTurn {

    public DemeterTurn(Turn turn) {
        super(turn);
    }

    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    public boolean build(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        Coordinate cx = c;
        String x,y;
        boolean nopower = !super.build(m,w,c);
        if(!nopower) return false;
        do{
            System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
            x=scanner.nextLine();
            y=scanner.nextLine();
        }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0 || (Integer.parseInt(x) == cx.getX() && Integer.parseInt(y) == cx.getY()));
        Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
        return true;
    }

    public boolean move(Match m, Worker w, Coordinate c) { return super.move(m, w, c); }

    public boolean limited_move(Match m, Worker w, Coordinate c){ return super.limited_move(m, w, c); }
}
