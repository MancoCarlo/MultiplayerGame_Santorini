package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class CharonTurn extends GodTurn{

    public CharonTurn(Turn turn){super(turn);}

    /**
     * allows to change an adjacent enemy worker position to the opposite side of the worker before moving him
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if the worker is moved, else false
     */
    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn) {
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
        ArrayList<Coordinate> coordinatesWorkers = new ArrayList<Coordinate>();
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                Coordinate c = new Coordinate(i, j);
                if (!m.getBoard()[c.getX()][c.getY()].isEmpty() && c.isNear(p.getWorker(wID).getPosition()) && !m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer().equals(p.getWorker(wID).getIDplayer())) {
                    Coordinate c1 = c.nextCoordinate(m, p.getWorker(wID).getPosition());
                    if(!m.getBoard()[c.getX()][c.getY()].getLocation().equals(c1) && m.getBoard()[c1.getX()][c1.getY()].isEmpty() && m.getBoard()[c1.getX()][c1.getY()].getLevel() != 4 )
                    coordinatesWorkers.add(new Coordinate(i, j));
                }
            }
        }
        if(coordinatesWorkers.size()!=0)
        {
            server.write(ch, "serviceMessage", "MSGE-You can use Charon's power\n");
            server.write(ch, "serviceMessage", "LIST-1) YES\n2)NO\n");
            server.write(ch,"interactionServer", "INDX-Would you like to move an adjacent enemy worker in the opposite box? ");

            String answer = server.read(ch);

            while(!answer.equals("1") && !answer.equals("2")){
                if(answer == null){
                    for(ClientHandler chl : server.getClientHandlers()){
                        server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                    }
                    ch.resetConnected();
                    ch.closeConnection();
                    return false;
                }else{
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
                    server.write(ch, "interactionServer", "INDX-Would you like to move an adjacent enemy worker in the opposite box? ");
                    answer = server.read(ch);
                }
            }

            Coordinate c;
            int id;
            if(answer.equals("1")){
                server.write(ch, "serviceMessage", "MSGE-Charon's power activated \n");
                server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinatesWorkers));
                server.write(ch, "interactionServer", "TURN-Choose the position of the worker you would like to move: \n");
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
                        if(id<0 || id>=coordinatesWorkers.size()){
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
                c = coordinatesWorkers.get(id);
                m.updateMovement(m.getPlayer(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()), m.getBoard()[c.getX()][c.getY()].getWorkerBox().getID(), c.nextCoordinate(m, p.getWorker(wID).getPosition()));
            }
        }
        for(ClientHandler clientHandler : server.getClientHandlers()){
            server.write(clientHandler, "serviceMessage", "BORD-"+m.printBoard());
        }
        Coordinate c = null;
        if(wID==0){
            server.write(ch, "serviceMessage", "MSGE-Move: \n");
            coordinates0 = whereCanMove(m, ch, 0, athenaOn);
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates0));
            server.write(ch, "interactionServer", "TURN-Where you want to move?\n");
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
            server.write(ch, "serviceMessage", "MSGE-Move: \n");
            coordinates1 = whereCanMove(m, ch, 0, athenaOn);
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates1));
            server.write(ch, "interactionServer", "TURN-Where you want to move?\n");
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

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
    }

}
