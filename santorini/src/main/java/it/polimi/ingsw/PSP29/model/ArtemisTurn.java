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
        String x, y, answer;
        Coordinate cx = w.getPosition();
        boolean nopower = super.move(m,w,c);
        if(!nopower) return false;
        System.out.println("Vuoi usare il potere di Artemis? 1) SI 2) NO\n");
        answer = scanner.nextLine();
        if(answer.equals("1")){
            if(!super.cantMove(m,w,false)){
                do{
                    System.out.println("Potere Artemis attivato!!\nInserisci una nuova coordinata x: \t");
                    x=scanner.nextLine();
                    System.out.println("Inserisci una nuova coordinata y: ");
                    y=scanner.nextLine();
                }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0 || (Integer.parseInt(x) == cx.getX() && Integer.parseInt(y) == cx.getY()) );
                Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
                return super.move(m,w,c1) || nopower;
            }else{
                System.out.println("Non puoi utilizzare il potere di Artemis");
            }
        }
        return true;
    }

    public boolean limited_move(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String x, y, answer;
        Coordinate cx = w.getPosition();
        boolean nopower = super.limited_move(m,w,c);
        if(!nopower) return false;
        System.out.println("Vuoi usare il potere di Artemis? 1) SI 2) NO\n");
        answer = scanner.nextLine();
        if(answer.equals("1")){
            if(!super.cantMove(m,w,false)){
                do{
                    System.out.println("Potere Artemis attivato!!\nInserisci una nuova coordinata x: \t");
                    x=scanner.nextLine();
                    System.out.println("Inserisci una nuova coordinata y: ");
                    y=scanner.nextLine();
                }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0 || (Integer.parseInt(x) == cx.getX() && Integer.parseInt(y) == cx.getY()) );
                Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
                return super.limited_move(m,w,c1) || nopower;
            }else{
                System.out.println("Non puoi utilizzare il potere di Artemis");
            }
        }
        return true;
    }
}
