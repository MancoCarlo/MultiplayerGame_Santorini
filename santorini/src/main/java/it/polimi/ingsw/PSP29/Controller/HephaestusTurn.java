package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class HephaestusTurn extends GodTurn {
    public HephaestusTurn(Turn turn) {
        super(turn);
    }


    /**
     * allows w to build two times but it has to be in the same box and not a dome
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
   /* @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        boolean nopower = super.build(m,w,c);
        if(!nopower) return false;
        Scanner scanner = new Scanner(System.in);
        String answer;
        if(m.getBoard()[c.getX()][c.getY()].getLevel()<3) { //non posso costruire una cupola
            System.out.println("Vuoi usare il potere di Hephaestus? 1) SI 2) NO\n");
            answer = scanner.nextLine();
            if (answer.equals("1")) {
                m.updateBuilding(c);
            }
        }
        return true;
    }
*/
}
