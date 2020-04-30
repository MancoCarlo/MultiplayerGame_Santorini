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
    public int ask_x_y() {
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try {
            s = scanner.nextLine();
            i = Integer.parseInt(s);
            if (i < 0 || i > 4) {
                throw (new NotValidInputException(0, 4));
            } else {
                return i;
            }
        } catch (NotValidInputException e) {
            return ask_x_y();
        }
    }
}
