package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.InputControl.Input;
import it.polimi.ingsw.PSP29.model.*;

import java.util.Scanner;

public class ArtemisTurn extends GodTurn{

    public ArtemisTurn(Turn turn) {
        super(turn);
    }

    /**
     * call winCondition() of the superclass
     * @param m match played
     * @param p player that play the turn
     * @return true if p win the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    /**
     * call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w can build in c, else false
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c){
        return super.build(m, w, c);
    }

    /**
     * w can make 2 movement, but can't return in his initial position
     * @param m match played
     * @param w worker that must be moved
     * @param c first movement of w
     * @return true if w can make 2 movement, else false
     */
    @Override
    public boolean move(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String x, y, answer;
        Coordinate cx = w.getPosition();
        boolean nopower = super.move(m,w,c);
        if(!nopower) return false;
        System.out.println("Vuoi usare il potere di Artemis? 1) SI 2) NO");
        /*answer = scanner.nextLine();
        if(answer.equals("1")){
            Coordinate c1;
            if(!super.cantMove(m,w,false)){
                do{
                    Input i = new Input();
                    c1 = i.askCoordinate("Potere Artemis");
                }while((c1.getX() == cx.getX() && c1.getY() == cx.getY()) || !super.move(m,w,c1) );
            }else{
                System.out.println("Non puoi utilizzare il potere di Artemis");
            }
        }*/
        return true;
    }

    /**
     *  w can make 2 movement, but can't return in his initial position and all the box where w pass have the same level
     * @param m match played
     * @param w worker that must be moved
     * @param c first movement of w
     * @return true if w can make 2 movement, else false
     */
    public boolean limited_move(Match m, Worker w, Coordinate c){
        Scanner scanner = new Scanner(System.in);
        String answer;
        Coordinate cx = w.getPosition();

        boolean nopower = super.limited_move(m,w,c);
        if(!nopower) return false;
        System.out.println("Vuoi usare il potere di Artemis? 1) SI 2) NO\n");
        answer = scanner.nextLine();
        /*if(answer.equals("1")){
            Coordinate c1;
            if(!super.cantMove(m,w,false)){
                do{
                    Input i = new Input();
                    c1 = i.askCoordinate("(Potere Artemis)");
                }while((c1.getX() == cx.getX() && c1.getY() == cx.getY()) || !super.limited_move(m,w,c1));
            }else{
                System.out.println("Non puoi utilizzare il potere di Artemis");
            }
        }*/
        return true;
    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param c coordinate that must be checked
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean canMoveTo(Match match,Worker w,Coordinate c, boolean athena){
        if(!athena){
            if(match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<=1){
                if(w.getMoved()){
                    if(c.equals(w.getPrev_position())){
                        return false;
                    }
                    else{
                        return true;
                    }
                }else{
                    return true;
                }
            }
        } else{
            if(match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<1){
                if(w.getMoved()){
                    if(c.equals(w.getPrev_position())){
                        return false;
                    }
                    else{
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }
        return false;
    }
}
