package it.polimi.ingsw.PSP29.model;

import java.util.Scanner;

public class DemeterTurn extends GodTurn {


    public DemeterTurn(Turn turn) {
        super(turn);
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
     * call build() of the superclass and after call another one time build() of the superclass with a new Coordinate
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w can build in c, else false
     */
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
