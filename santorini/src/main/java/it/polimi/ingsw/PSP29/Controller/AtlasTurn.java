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
        server.write(ch, "serviceMessage", "MSGE-Build: \n");
        if(coordinates.size()!=0){
            Coordinate c = null;
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates));
            server.write(ch, "interactionServer", "TURN-Where you want to build?\n");
            int id;
            while(true){
                try{
                    String msg = server.read(ch);
                    if(msg == null){
                        for(ClientHandler chl : server.getClientHandlers()){
                            server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                        }
                        ch.resetConnected();
                        ch.closeConnection();
                        return false;
                    }else{
                        id = Integer.parseInt(msg);
                    }
                    if(id<0 || id>=coordinates.size()){
                        server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                        server.write(ch, "interactionServer", "INDX-Try another index: ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "interactionServer", "INDX-Try another index: ");
                }
            }
            server.write(ch, "serviceMessage", "MSGE-You can use Atlas's power\n");
            server.write(ch, "serviceMessage", "LIST-1) YES\n2)NO\n");
            server.write(ch, "interactionServer", "INDX2Would you use Atlas's power? (You can build a level 4 building) ");
            String response = server.read(ch);
            c = coordinates.get(id);
            if(response == null){
                for(ClientHandler chl : server.getClientHandlers()){
                    server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                }
                ch.resetConnected();
                ch.closeConnection();
                return false;
            }
            if(response.equals("1")){
                while(m.getBoard()[c.getX()][c.getY()].getLevel() < 4){
                    m.updateBuilding(c);
                }
            }else{
                m.updateBuilding(c);
            }
            m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            p.getWorker(wID).changeBuilt(true);
            return true;
        }else{
            return false;
        }
    }
}
