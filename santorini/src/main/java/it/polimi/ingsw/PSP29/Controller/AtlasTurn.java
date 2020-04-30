package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;
import java.util.Scanner;

public class AtlasTurn extends GodTurn{

    public AtlasTurn(Turn turn) {
        super(turn);
    }

    /**
     * call build() of the superclass while level of the box in c is 4 or simply call build()
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @return true if w can build in c, else false
     */
    @Override
    public boolean build(Match m, ClientHandler ch, Server server){
        int wID=2;
        Player p = m.getPlayer(ch.getName());
        if(p.getWorker(0).getMoved()) wID = 0;
        if(p.getWorker(1).getMoved()) wID = 1;
        ArrayList<Coordinate> coordinates = whereCanBuild(m, ch, wID);
        server.write(ch, "serviceMessage", "Build: ");
        if(coordinates.size()!=0){
            Coordinate c = null;
            server.write(ch, "serviceMessage", printCoordinates(coordinates));
            server.write(ch, "interactionServer", "Where you want to build?\n");
            int id;
            while(true){
                try{
                    id = Integer.parseInt(server.read(ch));
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
            server.write(ch, "interactionServer", "Would you use Atlas's power?\n1) Yes\n2) No\n ");
            String response = server.read(ch);
            if(response.equals("1")){
                while(m.getBoard()[c.getX()][c.getY()].getLevel() < 4){
                    m.updateBuilding(c);
                }
            }else{
                c = coordinates.get(id);
                m.updateBuilding(c);
            }
            m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            p.getWorker(wID).changeBuilt(true);
            server.write(ch,"serviceMessage", m.printBoard());
            return true;
        }else{
            return false;
        }
    }
}
