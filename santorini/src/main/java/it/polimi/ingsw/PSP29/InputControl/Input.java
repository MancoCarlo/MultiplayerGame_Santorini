package it.polimi.ingsw.PSP29.InputControl;

import it.polimi.ingsw.PSP29.Controller.*;
import it.polimi.ingsw.PSP29.model.*;

import java.util.Scanner;

public class Input {

    public Input(){ }
    /**
     *
     * read the Coordinate from the player input
     *
     * @param str used for output line
     */
    public Coordinate askCoordinate(String str){
        int x=0, y=0;
        System.out.println(" inserisci la X dove vuoi " + str + ": ");
        x=ask_x_y();
        System.out.println(" inserisci la Y dove voui " + str + ": ");
        y=ask_x_y();
        return new Coordinate(x, y);
    }

    /**
     *
     * used to ask to the player the x or the y of the coordinate
     */
    public int ask_x_y(){
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try{
            s=scanner.nextLine();
            i=Integer.parseInt(s);
            if(i<0 || i>4){
                throw (new NotValidInputException(0, 4));
            }
            else{
                return i;
            }
        }
        catch(NotValidInputException e){
            return ask_x_y();
        }
    }

    /**
     *
     * used to ask to the player wich of his workers he wants to move
     *
     * @param p the player that plays the turn
     */
    public int askWorker(Match m, Player p, Turn turn, boolean Athena){
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try{
            System.out.println(p.getNickname() + " con che worker vuoi muoverti e costruire: ");
            if(!turn.cantMove(m, p.getWorker(0), Athena)){
                System.out.println("0) Worker in posizione " + p.getWorker(0).getPosition().getX() + "," + p.getWorker(0).getPosition().getY() );
            }
            if(!turn.cantMove(m, p.getWorker(1), Athena)){
                System.out.println("1) Worker in posizione " + p.getWorker(1).getPosition().getX() + "," + p.getWorker(1).getPosition().getY() );
            }
            s=scanner.nextLine();
            i=Integer.parseInt(s);
            if(i!=0 && i!=1){
                throw (new NotValidInputException(0, 1));
            }
            else{
                return i;
            }
        }
        catch (NotValidInputException e){
            return askWorker(m, p, turn, Athena);
        }
    }

    /**
     * used to ask if the player wants to use his god
     */
    public boolean askGod(){
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try{
            System.out.println("Vuoi usare la tua divinità: ");
            System.out.println("1) Sì\n2) No");
            s=scanner.nextLine();
            i=Integer.parseInt(s);
            if(i!=1 && i!=2){
                throw (new NotValidInputException(1, 2));
            }
            else{
                return(i == 1);
            }
        }
        catch(NotValidInputException e){
            return askGod();
        }
    }
}
