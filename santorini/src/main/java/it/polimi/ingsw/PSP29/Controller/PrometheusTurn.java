package it.polimi.ingsw.PSP29.Controller;

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
        Player p = m.getPlayer(ch.getName());
        ArrayList<Coordinate> coordinates0 = whereCanMove(m, ch, 0, athenaOn);
        ArrayList<Coordinate> coordinates1 = whereCanMove(m, ch, 1, athenaOn);
        if(coordinates0.size()!=0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "MSGE-It's your turn\n");
            server.write(ch, "serviceMessage", "LIST-"+m.getPlayer(ch.getName()).printWorkers());
            server.write(ch, "interactionServer", "INDX2Choose the worker to use in this turn: \n");
            while(true){
                try{
                    String msg = server.read(ch);
                    if(msg == null){
                        ch.resetConnected();
                        ch.closeConnection();
                        return false;
                    }else{
                        wID= Integer.parseInt(msg) - 1;
                    }
                    if(wID<0 || wID>1){
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
        if(!p.getWorker(wID).canLevelUp(m) || athenaOn){
            String power;
            server.write(ch, "serviceMessage", "MSGE-You can use Prometheus power\n");
            server.write(ch, "serviceMessage", "LIST-1)YES\n2) NO\n");
            server.write(ch, "interactionServer", "INDX2-Would you like to build an additional block before moving you worker? ");
            power = server.read(ch);
            if(power.equals("1"))
            {
               int count = 0;
                ArrayList<Coordinate> coordinates = whereCanBuild(m, ch, wID);
                for(Coordinate c : coordinates) {
                    if ( m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel() < 3 && m.getBoard()[c.getX()][c.getY()].getLevel() <= m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel())
                        //se il worker ha livello inferiore a 3 e se la casella ha un livello inferirore o uguale a quello del mio worker
                        continue;
                    else {
                        if ( m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel() == 3 && m.getBoard()[c.getX()][c.getY()].getLevel() < m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel())
                            continue;//se il worker è al terzo livello e la casella ha un livello inferiore al worker
                        if (m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel() == 3 && m.getBoard()[c.getX()][c.getY()].getLevel() == 3) {
                            count = count +1;
                            continue;//se worker non può salire di livello ed è al terzo livello e la casella considerata è al 3 livello la includo inizialmente
                        }
                        else
                            coordinates.remove(c);
                    }
                }
                if(!athenaOn) {
                    if (count == 1 && coordinates.size() == 1) //se c'è solo una casella disponibile ed è al terzo livello non posso usare il potere
                        server.write(ch, "serviceMessage", "MSGE-You can't use the power of Prometheus \n");
                } else if(coordinates.size()== 1 && m.getBoard()[p.getWorker(wID).getPosition().getX()][p.getWorker(wID).getPosition().getY()].getLevel() == m.getBoard()[coordinates.get(0).getX()][coordinates.get(0).getY()].getLevel())
                        server.write(ch, "serviceMessage", "MSGE-You can't use the power of Prometheus \n");
                else{
                    server.write(ch, "serviceMessage", "MSGE-Additional Build: ");
                    if(coordinates.size()!=0){
                        Coordinate c = null;
                        server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates));
                        server.write(ch, "interactionServer", "INDX2Where you want to build?\n");
                        int id;
                        while(true){
                            try{
                                id = Integer.parseInt(server.read(ch));
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
                        c = coordinates.get(id);
                        m.updateBuilding(c);
                    } else{
                        server.write(ch, "serviceMessage", "MSGE-You can't build an additional block\n");
                    }
                }
            }
        }
        Coordinate c = null;
        if(wID==0){
            coordinates0 = whereCanMove(m, ch, 0, athenaOn);
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates0));
            server.write(ch, "interactionServer", "TURN-Where you want to move?\n");
            int id;
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
                    if(id<0 || id>=coordinates0.size()){
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
            c = coordinates0.get(id);
        }
        else if(wID==1){
            coordinates1 = whereCanMove(m, ch, 1, athenaOn);
            server.write(ch, "serviceMessage", printCoordinates(coordinates1));
            server.write(ch, "interactionServer", "Where do you want to move?\n");
            int id;
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
                    if(id<0 || id>=coordinates1.size()){
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
            c = coordinates1.get(id);
        }
        m.updateMovement(p,wID,c);
        p.getWorker(wID).changeMoved(true);
        return true;
    }

}
