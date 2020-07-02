package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class MinotaurTurn extends GodTurn {

    public MinotaurTurn(Turn turn) {
        super(turn);
    }

    /**
     * move the worker with or without Minotaur power
     * @param m match played
     * @param ch owner of the turn
     * @param server manage the interaction with client
     * @param athenaOn true if athena is on
     * @return true if the worker is moved, else false
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
            server.write(ch, "interactionServer", "TURN-Choose the worker to use in this turn: \n");
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
                        server.write(ch, "interactionServer", "TURN-Try another index: ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                    server.write(ch, "interactionServer", "TURN-Try another index: ");
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
        server.write(ch, "serviceMessage", "BORD-"+m.printBoard());
        Coordinate c = null;
        if(wID==0){
            server.write(ch, "serviceMessage", "MSGE-Move: \n");
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates0));
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
                    if(id<0 || id>=coordinates0.size()){
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
            c = coordinates0.get(id);
        }
        else if(wID==1){
            server.write(ch, "serviceMessage", "MSGE-Move: \n");
            server.write(ch, "serviceMessage", "LIST-"+printCoordinates(coordinates1));
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
                    if(id<0 || id>=coordinates1.size()){
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
            c = coordinates1.get(id);
        }
        if(!m.getBoard()[c.getX()][c.getY()].isEmpty()) {
            server.write(ch, "serviceMessage", "MSGE-You're using Mintaurus Power!\n");
            Coordinate c1 = p.getWorker(wID).getPosition().nextCoordinate(m, c);
            m.updateMovement(m.getPlayer(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()), m.getBoard()[c.getX()][c.getY()].getWorkerBox().getID(), c1);
            m.updateMovement(m.getPlayer(p.getWorker(wID).getIDplayer()), wID, c);
            p.getWorker(wID).changeMoved(true);
            return true;
        }
        else {
            m.updateMovement(p, wID, c);
            p.getWorker(wID).changeMoved(true);
            return true;
        }
    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param c coordinate that must be checked
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location with or without using Minotaur power, else false
     */
    @Override
    public boolean canMoveTo(Match match, Worker w, Coordinate c, boolean athena){
        if(!athena){
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<=1)
                return true;
            if (w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].getLevel() != 4) { //se la casella è adiacente ma non coincidente e se la torre non è completa
                Coordinate c1 = w.getPosition().nextCoordinate(match, match.getBoard()[c.getX()][c.getY()].getLocation());//restituisce la casella stessa se non ha una successiva
                if (!match.getBoard()[c.getX()][c.getY()].getLocation().equals(c1) && match.getBoard()[c1.getX()][c1.getY()].isEmpty() && !match.getBoard()[c.getX()][c.getY()].isEmpty() && !match.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer().equals(w.getIDplayer()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[c.getX()][c.getY()]) >= -1 && match.getBoard()[c1.getX()][c1.getY()].getLevel() != 4) {
                    //se la casella c ha una successiva e se la casella successiva  è vuota e se la casella indicata contiene un operaio e se l'operaio nella casella non è dello stesso giocatore e se la casella in cui mi voglio spostare non è piu alta del mio livello e se la casella successiva non è completa
                    return true;
                }
            }
        } else{
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<1)
                return true;
            if (w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].getLevel() != 4) {
                Coordinate c1 = w.getPosition().nextCoordinate(match, match.getBoard()[c.getX()][c.getY()].getLocation());
                if (!match.getBoard()[c.getX()][c.getY()].getLocation().equals(c1) && match.getBoard()[c1.getX()][c1.getY()].isEmpty() && !match.getBoard()[c.getX()][c.getY()].isEmpty() && !match.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer().equals(w.getIDplayer()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[c.getX()][c.getY()]) > -1 && match.getBoard()[c1.getX()][c1.getY()].getLevel() != 4) {
                    //se la casella c ha una successiva e se la casella successiva  è vuota e se la casella indicata contiene un operaio e se l'operaio nella casella non è dello stesso giocatore e se la casella in cui mi voglio spostare non è alta di più di 1 livello e se la casella successiva non è completa
                    return true;
                    }
                }

        }
        return false;
    }
}


