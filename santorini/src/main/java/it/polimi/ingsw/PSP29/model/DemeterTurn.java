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
        String x,y, answer;
        boolean nopower = super.build(m,w,c);
        if(!nopower) return false;
        if(w.canBuild(m)){
            System.out.println("Vuoi usare il potere di Demeter? 1) SI 2) NO");
            answer = scanner.nextLine();
            if(answer.equals("1")){
                do{
                    System.out.println("Potere Demeter attivato!!\nInserisci una nuova coordinata x: \t");
                    x=scanner.nextLine();
                    System.out.println("Inserisci una nuova coordinata y: ");
                    y=scanner.nextLine();
                }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0 || (Integer.parseInt(x) == cx.getX() && Integer.parseInt(y) == cx.getY()));
                Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
                return super.build(m,w,c1) || nopower;
            }
        }
        return true;
    }

    public boolean move(Match m, Worker w, Coordinate c) { return super.move(m, w, c); }

    public boolean limited_move(Match m, Worker w, Coordinate c){ return super.limited_move(m, w, c); }
}
