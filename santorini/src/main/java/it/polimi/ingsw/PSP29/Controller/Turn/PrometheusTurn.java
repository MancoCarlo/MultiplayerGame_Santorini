package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class PrometheusTurn extends GodTurn {

    public PrometheusTurn(Turn turn) {
        super(turn);
    }

    /**
     * allows a player to build before and after moving his worker if in this turn his worker can't level up
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if is moved in c, else false
     */

    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn){
        int wID=2;
        String power = "";
        Player p = m.getPlayer(ch.getName());
        ArrayList<Coordinate> coordinates0 = whereCanMove(m, ch, 0, athenaOn);
        ArrayList<Coordinate> coordinates1 = whereCanMove(m, ch, 1, athenaOn);
        if(coordinates0.size()!=0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "MSGE-It's your turn\n");
            server.write(ch, "serviceMessage", "LIST-"+m.getPlayer(ch.getName()).printWorkers());
            server.write(ch, "interactionServer", "TURN-Choose the worker to use in this turn: \n");
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
                    wID= Integer.parseInt(msg) - 1;
                }
                if(wID<0 || wID>1){
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "interactionServer", "TURN-Try another index: ");
                    continue;
                }
                break;
            }
        }
        else if(coordinates0.size()!=0 && coordinates1.size()==0){
            server.write(ch, "serviceMessage", "MSGE-You can only move one of your worker in these positions: \n");
            wID = 0;
        }
        else if(coordinates0.size()==0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "MSGE-You can only move one of your worker in these positions: \n");
            wID = 1;
        }else if(coordinates0.size()==0 && coordinates1.size()==0){
            return false;
        }

        server.write(ch, "serviceMessage", "BORD-"+m.printBoard());

        ArrayList<Coordinate> controlCoor = whereCanMove(m,ch,wID,true);
        ArrayList<Coordinate> controlBCoor = whereCanBuild(m,ch,wID);

        if(controlCoor.size()==1){
            //there is only one box where i can build after activate the power and then maybe move because i'm prometheus
            if(controlBCoor.size()==1 && m.getBoard()[controlCoor.get(0).getX()][controlCoor.get(0).getY()].getLevel() - m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel() > -1){
                //I can build in only one box and that box have not an inferior level than me, so i block me and i have violed prometheus power
                server.write(ch, "serviceMessage", "MSGE-You can't use Prometheus power\n");
            }else{
                //I can build in only one box and that box have an inferior level than me, so i can build and then not move up
                server.write(ch, "serviceMessage", "MSGE-You can use Prometheus power\n");
                server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
                server.write(ch, "interactionServer", "INDX-Would you like to build an additional block before moving you worker? ");
                power = server.read(ch);
                if(power == null){
                    for(ClientHandler chl : server.getClientHandlers()){
                        server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                    }
                    ch.resetConnected();
                    ch.closeConnection();
                    return false;
                }

                while(!power.equals("1") && !power.equals("2")){
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
                    server.write(ch, "interactionServer", "INDX-Would you like to build an additional block before moving your worker? ");
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

                if(power.equals("1")) {
                    ArrayList<Coordinate> finalcoordinates = whereCanBuild(m, ch, wID);

                    if(m.getBoard()[controlCoor.get(0).getX()][controlCoor.get(0).getY()].getLevel() - m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel() > -1){
                        int coorDelete = -1;
                        for(int i = 0; i<finalcoordinates.size();i++){
                            if(finalcoordinates.get(i).equals(controlCoor.get(0)))
                                coorDelete = i;
                        }

                        finalcoordinates.remove(coorDelete);
                    }

                    server.write(ch, "serviceMessage", "MSGE-Additional Build: \n");
                    Coordinate c;
                    server.write(ch, "serviceMessage", "LIST-"+printCoordinates(finalcoordinates));
                    server.write(ch, "interactionServer", "TURN-Where you want to build?\n");
                    int id;
                    while(true){
                        String msg = server.read(ch);
                        if(msg == null){
                            for(ClientHandler chl : server.getClientHandlers()){
                                server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                            }
                            ch.resetConnected();
                            ch.closeConnection();
                            return false;
                        }
                        id = Integer.parseInt(msg);
                        if(id<0 || id>=finalcoordinates.size()){
                            server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                            server.write(ch, "interactionServer", "TURN-Try another index: ");
                            continue;
                        }
                        break;
                    }
                    c = finalcoordinates.get(id);
                    m.updateBuilding(c);
                }
                for(ClientHandler clientHandler : server.getClientHandlers()) {
                    server.write(clientHandler, "serviceMessage", "BORD-" + m.printBoard());
                }
            }
        }else{
            if(controlCoor.size() == 0)
                server.write(ch, "serviceMessage", "MSGE-You can't use Prometheus power\n");
            else{
                server.write(ch, "serviceMessage", "MSGE-You can use Prometheus power\n");
                server.write(ch, "serviceMessage", "LIST-1)YES\n2) NO\n");
                server.write(ch, "interactionServer", "INDX-Would you like to build an additional block before moving you worker? ");
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
                        server.write(ch, "interactionServer", "INDX-Would you like to build an additional block before moving your worker? ");
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
                    ArrayList<Coordinate> finalcoordinates = whereCanBuild(m, ch, wID);
                    server.write(ch, "serviceMessage", "MSGE-Additional Build: \n");
                    Coordinate c;
                    server.write(ch, "serviceMessage", "LIST-"+printCoordinates(finalcoordinates));
                    server.write(ch, "interactionServer", "TURN-Where you want to build?\n");
                    int id;
                    while(true){
                        String msg = server.read(ch);
                        if(msg == null){
                            for(ClientHandler chl : server.getClientHandlers()){
                                server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                            }
                            ch.resetConnected();
                            ch.closeConnection();
                            return false;
                        }
                        id = Integer.parseInt(msg);

                        if(id<0 || id>=finalcoordinates.size()){
                            server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                            server.write(ch, "interactionServer", "TURN-Try another index: ");
                            continue;
                        }
                        break;
                    }
                    c = finalcoordinates.get(id);
                    m.updateBuilding(c);
                }
                for(ClientHandler clientHandler : server.getClientHandlers()) {
                    server.write(clientHandler, "serviceMessage", "BORD-" + m.printBoard());
                }
            }
        }

        Coordinate c = null;
        if(wID==0){
            if(power.equals("1"))
                coordinates0 = whereCanMove(m, ch, 0, true);
            else
                coordinates0 = whereCanMove(m, ch, 0, athenaOn);
            server.write(ch, "serviceMessage", "MSGE-Move: \n");
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates0));
            server.write(ch, "interactionServer", "TURN-Where you want to move?\n");
            int id;
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
                if(id<0 || id>=coordinates0.size()){
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "interactionServer", "TURN-Try another index: ");
                    continue;
                }
                break;
            }
            c = coordinates0.get(id);
        }
        else if(wID==1){
            if(power.equals("1"))
                coordinates1 = whereCanMove(m, ch, 1, true);
            else
                coordinates1 = whereCanMove(m, ch, 1, athenaOn);
            server.write(ch, "serviceMessage", "MSGE-Move: \n");
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates1));
            server.write(ch, "interactionServer", "TURN-Where do you want to move?\n");
            int id;
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
                if(id<0 || id>=coordinates1.size()){
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "interactionServer", "TURN-Try another index: ");
                    continue;
                }
                break;
            }
            c = coordinates1.get(id);
        }
        m.updateMovement(p,wID,c);
        p.getWorker(wID).changeMoved(true);
        return true;
    }

}
