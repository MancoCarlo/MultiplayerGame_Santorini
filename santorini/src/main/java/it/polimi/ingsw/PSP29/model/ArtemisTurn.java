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
        super.move(m,w,c);
        do{
            System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
            x=scanner.nextLine();
            y=scanner.nextLine();
        }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0);
        Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
        if(c1.equals(cx) || !w.getPosition().isNear(c1) || m.getBoard()[c1.getX()][c1.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4 || !m.getBoard()[c1.getX()][c1.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c1);
            w.changeMoved();
            return true;
        }
    }

    public boolean limited_move(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String x, y;
        Coordinate cx = w.getPosition();
        super.limited_move(m,w,c);
        do{
            System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
            x=scanner.nextLine();
            y=scanner.nextLine();
        }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0);
        Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
        if(c1.equals(cx) || !w.getPosition().isNear(c1) || m.getBoard()[c1.getX()][c1.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4 || !m.getBoard()[c1.getX()][c1.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c1);
            w.changeMoved();
            return true;
        }
    }
}
