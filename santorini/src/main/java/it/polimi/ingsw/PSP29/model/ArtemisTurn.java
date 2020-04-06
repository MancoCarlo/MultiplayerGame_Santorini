package it.polimi.ingsw.PSP29.model;

import java.util.ArrayList;
import java.util.Scanner;

public class ArtemisTurn extends GodTurn{

    public ArtemisTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c){
        return super.build(m, w, c);
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String x, y;
        Coordinate cx = w.getPosition();
        boolean nopower = super.move(m,w,c);
        if(!nopower) return false;
        if(!super.cantMove(m,w,false)){
            do{
                System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
                x=scanner.nextLine();
                y=scanner.nextLine();
            }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0 || (Integer.parseInt(x) == cx.getX() && Integer.parseInt(y) == cx.getY()) );
            Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
            return super.move(m,w,c1) || nopower;
        }else return true;
    }

    public boolean limited_move(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String x, y;
        Coordinate cx = w.getPosition();
        boolean nopower = super.limited_move(m,w,c);
        if(!nopower) return false;
        if(!super.cantMove(m,w,true)){
            do{
                System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
                x=scanner.nextLine();
                y=scanner.nextLine();
            }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0 || (Integer.parseInt(x) == cx.getX() && Integer.parseInt(y) == cx.getY()));
            Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
            return super.limited_move(m,w,c1) || nopower;
        }else return true;
    }
}
