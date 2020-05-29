package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;


public class TritonTurn extends GodTurn {

    public TritonTurn(Turn turn) {super(turn);}

    /**
     * move the worker and allows him to move unlimited times on border boxes if he was moved on one of them
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if the worker is moved at least once
     */
    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn) {
        Player p = m.getPlayer(ch.getName());
        int wID=2;
        boolean nopower= super.move(m, ch, server, athenaOn);
        if(!nopower)
            return false;
        ArrayList<Coordinate> coordinates = null;
        for(Worker w : p.getWorkers()){
            if(w.getMoved()){
                wID = w.getID();
                break;
            }
        }
        String power;
        Coordinate c = null;
        do{
            if(super.winCondition(m, p)){
                return true;
            }
            power = "0";
            coordinates = whereCanMove(m, ch, wID, athenaOn);
            if(!(coordinates.size() == 0) && (p.getWorker(wID).getPosition().getX()==0 || p.getWorker(wID).getPosition().getX()==m.getRows()-1 || p.getWorker(wID).getPosition().getY()==0 || p.getWorker(wID).getPosition().getY()==m.getColumns()-1)) {
                for(ClientHandler clientHandler : server.getClientHandlers()){
                    server.write(clientHandler, "serviceMessage", "BORD-"+m.printBoard());
                }
                server.write(ch, "serviceMessage", "MSGE-You can use Triton power\n");
                server.write(ch, "serviceMessage", "LIST-1) YES\n2)NO\n");
                server.write(ch,"interactionServer", "INDX-Would you like to move again? ");
                power = server.read(ch);

                if(power == null){
                    for(ClientHandler chl : server.getClientHandlers()){
                        server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                    }
                    ch.resetConnected();
                    ch.closeConnection();
                    return false;
                }else{
                    while(!power.equals("1") && !power.equals("2")){
                        server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                        server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
                        server.write(ch, "interactionServer", "INDX-Would you like to move again? ");
                        power = server.read(ch);
                        if(power == null){
                            for(ClientHandler chl : server.getClientHandlers()){
                                server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                            }
                            ch.resetConnected();
                            ch.closeConnection();
                            return false;
                        }
                    }
                }

                if(power.equals("1")) {
                    server.write(ch, "serviceMessage", "MSGE-Triton's power activated \n");
                    server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates));
                    server.write(ch, "interactionServer", "TURN-Where do you want to move?\n");
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
                                server.write(ch, "interactionServer", "TURN-Try another index: ");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e){
                            server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                            server.write(ch, "interactionServer", "TURN-Try another index: ");
                        }
                    }
                    c = coordinates.get(id);
                    m.updateMovement(p,wID,c);
                }
            }
        }while(power.equals("1"));
        return true;
    }

    @Override
    public boolean canMoveTo(Match m, Worker w, Coordinate c, boolean athena){ return super.canMoveTo(m,w,c,athena);
    }

}
