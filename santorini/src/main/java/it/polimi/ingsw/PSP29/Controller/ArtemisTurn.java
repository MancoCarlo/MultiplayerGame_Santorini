package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.InputControl.Input;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class ArtemisTurn extends GodTurn{

    public ArtemisTurn(Turn turn) {
        super(turn);
    }

    /**
     * move the worker
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn){
        Player p = m.getPlayer(ch.getName());
        int wID=2;
        boolean nopower = super.move(m,ch,server,athenaOn);
        if(!nopower) return false;
        server.write(ch,"serviceMessage", m.printBoard());
        server.write(ch,"interactionServer", "Would you move again?\n1) Yes\n2) No\n");
        String answer = server.read(ch);
        if(answer.equals("1")){
            ArrayList<Coordinate> coordinates = null;
            for(Worker w : p.getWorkers()){
                if(w.getMoved()){
                    coordinates = whereCanMove(m, ch, w.getID(), athenaOn);
                    wID = w.getID();
                    break;
                }
            }
            if(coordinates.size()!=0){
                Coordinate c;
                int id;
                server.write(ch, "serviceMessage", printCoordinates(coordinates));
                server.write(ch, "interactionServer", "Where you want to move?\n");
                while(true){
                    try{
                        String msg = server.read(ch);
                        if(msg == null){
                            ch.resetConnected();
                            ch.closeConnection();
                            return false;
                        }else{
                            id = Integer.parseInt(msg);
                        }
                        if(id<0 || id>=coordinates.size()){
                            server.write(ch, "serviceMessage", "Invalid input\n");
                            server.write(ch, "interactionServer", "Try another index: ");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e){
                        server.write(ch, "serviceMessage", "Invalid input\n");
                        server.write(ch, "interactionServer", "Try another index: ");
                    }
                }
                c = coordinates.get(id);
                m.updateMovement(p,wID,c);
                p.getWorker(wID).changeMoved(true);
            }
        }
        return true;
    }
}
