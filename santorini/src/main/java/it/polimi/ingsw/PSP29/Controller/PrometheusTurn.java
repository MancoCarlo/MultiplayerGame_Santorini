package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class PrometheusTurn extends GodTurn {
    public PrometheusTurn(Turn turn) {
        super(turn);
    }

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    /**
     * call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w has built in c, else false
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return super.build(m, w, c);
    }

    /**
     * allows a player to build before and after moving his worker if in this turn his worker can't level up
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean move(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >0 || m.getBoard()[c.getX()][c.getY()].getLevel() == 4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false; //se la mossa non è valida
        }
        else {
            Scanner scanner = new Scanner(System.in);
            String x, y;
            do {
                System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata dove vuoi costruire: \t");
                x = scanner.nextLine();
                y = scanner.nextLine();
            } while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0);
            Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
            if((!w.getPosition().isNear(c1) || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4 || !m.getBoard()[c1.getX()][c1.getY()].isEmpty()) || (c.equals(c1) && m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >0)){
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved(true);
                return true; //mi muovo ma non costruisco
            }
            else{
                m.updateBuilding(c1);
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved(true); //costruisco e mi muovo
                return true;
            }
        }
    }

    /**
     * allows a player to build before and after moving his worker without letting him level up
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        Scanner scanner = new Scanner(System.in);
        String x, y;
        do {
            System.out.println("Potere dio attivato!!\nInserisci una nuova coordinata dove vuoi costruire: \t");
            x = scanner.nextLine();
            y = scanner.nextLine();
        }while(Integer.parseInt(x)>m.getRows()-1 || Integer.parseInt(y)>m.getRows()-1 || Integer.parseInt(x)<0 || Integer.parseInt(y)<0);
        Coordinate c1=new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >0 || m.getBoard()[c.getX()][c.getY()].getLevel() == 4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false; //se la mossa non è valida
        }
        else {
            if((!w.getPosition().isNear(c1) || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4 || !m.getBoard()[c1.getX()][c1.getY()].isEmpty()) || (c.equals(c1) && m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >=0)){
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved(true);
                return true; //mi muovo ma non costruisco
            }
            else{
                m.updateBuilding(c1);
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved(true); //costruisco e mi muovo
                return true;
            }
        }
    }

    /**
     * checks if a player can't move his worker using Prometheus power
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
   /* @Override
    public boolean cantMove(Match m,Worker w, boolean athena){
        int count = 0;
        if(!athena){
            for(int i=0; i<m.getRows(); i++){
                for(int j=0; j<m.getColumns(); j++){
                    if(m.getBoard()[i][j].isEmpty() && w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel()!=4 && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(m.getBoard()[i][j])==-1){
                        //se esiste una casella che è vuota, è vicina al mio operaio, la cui torre non è completa e il mio operaio puo salire di livello
                        return true;//non si può usare la divinità
                    }
                    else { // devo controllare che costruendo poi riesca a muoversi
                        if (!w.canLevelUp(m) && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() < 3 && m.getBoard()[i][j].isEmpty() && w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() <= m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel())
                            //se il worker non puo salire di livello ed è a livello inferiore a 3 e se esiste una casella che è vuota e vicina al mio operaio, il cui livello è inferirore o uguale a quello del mio worker
                            return false;
                        else {
                            if (!w.canLevelUp(m) && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() == 3 && m.getBoard()[i][j].isEmpty() && w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() < m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel())
                                return false;//se il worker non puo salire di livello ed è al terzo livello e c'è almeno una casella vuota e adiacente che è ha un livello inferiore
                            if (!w.canLevelUp(m) && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() == 3 && m.getBoard()[i][j].isEmpty() && w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() != 4)//almeno due caselle che non sono al 4 livello
                                count =count +1;//se worker non può salire di livello ed è al terzo livello e ci sono almeno due caselle che non sono al 4 livello
                        }
                    }
                }
            }
            if(count >=2)
                return false;
            else
                return true;
        }
        else {
            for(int i=0; i<m.getRows(); i++){
                for(int j=0; j<m.getColumns(); j++) {
                    if (m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() <= 3 && m.getBoard()[i][j].isEmpty() && w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() < m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel())
                        //se il worker è a livello inferiore o uguale a 3 e se esiste una casella che è vuota e vicina al mio operaio, il cui livello è inferirore a quello del mio worker
                        return false;
                    if (m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() == 3 && m.getBoard()[i][j].isEmpty() && w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() != 4)//almeno due caselle che non sono al 4 livello
                        count =count +1;//se worker è al terzo livello e ci sono almeno due caselle che non sono al 4 livello

                }
            }
            if(count >=2)
                return false;
            else
                return true;

        }
    }*/

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
    }

    @Override
    public String printCoordinates(ArrayList<Coordinate> coordinates) {
        return super.printCoordinates(coordinates);
    }

}
