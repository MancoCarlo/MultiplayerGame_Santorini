package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class ApolloTurn extends GodTurn{

    public ApolloTurn(Turn turn) {
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
        int wID=2;
        Player p = m.getPlayer(ch.getName());
        ArrayList<Coordinate> coordinates0 = whereCanMove(m, ch, 0, athenaOn);
        ArrayList<Coordinate> coordinates1 = whereCanMove(m, ch, 1, athenaOn);
        if(coordinates0.size()!=0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "It's your turn\n");
            server.write(ch, "interactionServer", m.getPlayer(ch.getName()).printWorkers());
            server.write(ch, "serviceMessage", "Choose the worker to use in this turn: \n");
            while(true){
                try{
                    wID = Integer.parseInt(server.read(ch));
                    if(wID<0 || wID>1){
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
        }
        else if(coordinates0.size()!=0 && coordinates1.size()==0){
            server.write(ch, "serviceMessage", "You can only move one of your worker in these positions: \n");
            wID = 0;
        }
        else if(coordinates0.size()==0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "You can only move one of your worker in these positions: \n");
            wID = 1;
        }else if(coordinates0.size()==0 && coordinates1.size()==0){
            return false;
        }
        Coordinate c = null;
        if(wID==0){
            server.write(ch, "serviceMessage", printCoordinates(coordinates0));
            server.write(ch, "interactionServer", "Where you want to move?\n");
            int id;
            while(true){
                try{
                    id = Integer.parseInt(server.read(ch));
                    if(id<0 || id>=coordinates0.size()){
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
            c = coordinates0.get(id);
        }
        else if(wID==1){
            server.write(ch, "serviceMessage", printCoordinates(coordinates1));
            server.write(ch, "interactionServer", "Where you want to move?\n");
            int id;
            while(true){
                try{
                    id = Integer.parseInt(server.read(ch));
                    if(id<0 || id>=coordinates1.size()){
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
            c = coordinates1.get(id);
        }
        if(m.getBoard()[c.getX()][c.getY()].isEmpty()){
            m.updateMovement(p,wID,c);
            p.getWorker(wID).changeMoved(true);
            server.write(ch,"serviceMessage", m.printBoard());
            return true;
        }else{
            Worker w2 = m.getBoard()[c.getX()][c.getY()].getWorkerBox();
            w2.setPosition(null);
            w2.setPrev_position(null);
            m.getBoard()[c.getX()][c.getY()].setWorkerBox(null);
            m.getBoard()[c.getX()][c.getY()].changeState();
            Coordinate cx = p.getWorker(wID).getPosition();
            m.updateMovement(p,wID,c);
            p.getWorker(wID).changeMoved(true);
            m.getPlayer(w2.getIDplayer()).putWorker(w2.getID(), m.getBoard(), cx);
            w2.setPrev_position(c);
            server.write(ch,"serviceMessage", m.printBoard());
            return true;
        }

    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param c coordinate that must be checked
     * @param athenaOn true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    @Override
    public boolean canMoveTo(Match match,Worker w,Coordinate c, boolean athenaOn){
        if(match.getBoard()[c.getX()][c.getY()].isEmpty())
            return super.canMoveTo(match,w,c,athenaOn);
        else{
            if(!athenaOn){
                if(match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<=1 && !w.getIDplayer().equals(match.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer())){
                    return true;
                }
            } else{
                if(match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<1 && !w.getIDplayer().equals(match.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer())){
                    return true;
                }
            }
            return false;
        }
    }
}

