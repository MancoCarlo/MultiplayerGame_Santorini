package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

/**
 * @author Carlo Manco
 */
public class ArtemisTurn extends GodTurn{

    public ArtemisTurn(Turn turn) {
        super(turn);
    }

    /**
     * can move the same worker twice, but not in the start box of the first movement
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
        for(ClientHandler clientHandler : server.getClientHandlers()){
            server.write(clientHandler, "serviceMessage", "BORD-"+m.printBoard());
        }
        if(super.winCondition(m, p)){
            return true;
        }
        server.write(ch, "serviceMessage", "MSGE-You can use Artemis power\n");
        server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
        server.write(ch, "interactionServer", "INDX-Would you move again in this turn? ");

        String answer = server.read(ch);
        if(answer == null){
            for(ClientHandler chl : server.getClientHandlers()){
                server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
            }
            ch.resetConnected();
            ch.closeConnection();
            return false;
        }else{
            while(!answer.equals("1") && !answer.equals("2")){
                server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
                server.write(ch, "interactionServer", "INDX-Would you move again in this turn? ");
                answer = server.read(ch);
                if(answer == null){
                    for(ClientHandler chl : server.getClientHandlers()){
                        server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                    }
                    ch.resetConnected();
                    ch.closeConnection();
                    return false;
                }
            }
        }

        if(answer.equals("1")){
            ArrayList<Coordinate> coordinates = null;
            for(Worker w : p.getWorkers()){
                if(w.getMoved()){
                    wID = w.getID();
                    coordinates = whereCanMove(m, ch, wID, athenaOn);
                    break;
                }
            }

            //remove the box where i was before the first movement
            int i;
            for(i = 0; i<coordinates.size();i++){
                if(coordinates.get(i).equals(p.getWorker(wID).getPrev_position())){
                    break;
                }
            }
            coordinates.remove(i);

            if(coordinates.size()!=0){
                Coordinate c;
                int id;
                server.write(ch, "serviceMessage", "MSGE-Move: \n");
                server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates));
                server.write(ch, "interactionServer", "TURN-Where you want to move?\n");
                while(true){
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
                        server.write(ch, "interactionServer", "TURN-Try another index: ");
                        continue;
                    }
                    break;
                }
                c = coordinates.get(id);
                m.updateMovement(p,wID,c);
                p.getWorker(wID).changeMoved(true);
            }
        }
        return true;
    }
}
