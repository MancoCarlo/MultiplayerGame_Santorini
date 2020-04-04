package it.polimi.ingsw.PSP29.model;

import java.util.Scanner;

public class PrometheusTurn extends GodTurn {
    public PrometheusTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return super.build(m, w, c);
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        if(!w.canLevelUp(m)) {
            Scanner scanner = new Scanner(System.in);
            String x, y;
            do {
                System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
                x = scanner.nextLine();
                y = scanner.nextLine();
            }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0);
            Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
            if(!w.getPosition().isNear(c1) || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4 || !m.getBoard()[c1.getX()][c1.getY()].isEmpty() || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) > 1 || m.getBoard()[c.getX()][c.getY()].getLevel() == 4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()) {
                return false; //se non riesco a costruire e muovermi. in questo modo non costruisco se non riesco a muovermi
            }
            else{
                m.updateBuilding(c1);
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved();
                return true;
            }
        }
        else
            return false; //return false perchè non uso il potere della divinità

    }

    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) { //stesso codice del move senza controllo con canLevelUp
        Scanner scanner = new Scanner(System.in);
        String x, y;
        do {
            System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata: \t");
            x = scanner.nextLine();
            y = scanner.nextLine();
        }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0);
        Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
        //controllo anche se le due coordinate sono uguali perche nel limitedTurn non si può salire di livello
        if(c1==c || !w.getPosition().isNear(c1) || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4 || !m.getBoard()[c1.getX()][c1.getY()].isEmpty() || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) > 1 || m.getBoard()[c.getX()][c.getY()].getLevel() == 4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()) {
            return false;
        }
        else{
            m.updateBuilding(c1);
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved();
            return true;
        }
    }

}
