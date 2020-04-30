package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.view.Client;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class BaseTurn implements Turn {

    /**
     * control if the player wins
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        for(Worker w : p.getWorkers()){
            if(w.getMoved() && w.getBuilt()){
                w.changeMoved(false);
                w.changeBuilt(false);
                if(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel()==3){
                    if(m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel()==3){
                        return true;
                    }
                    else if(!m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getlevelledUp() && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel()==2){
                        return true;
                    }
                    else{
                        m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].setLevelledUp(false);
                        return false;
                    }
                }
                m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].setLevelledUp(false);
                return false;
            }
        }
        return false;
    }

    /**
     * let the worker build
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateBuilding(c);
            m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            w.changeBuilt(true);
            return true;
        }
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
        m.updateMovement(p,wID,c);
        p.getWorker(wID).changeMoved(true);
        return true;
    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param c coordinate that must be checked
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    public boolean canMoveTo(Match match,Worker w,Coordinate c, boolean athena){
        if(!athena){
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<=1){
                return true;
            }
        } else{
            if(match.getBoard()[c.getX()][c.getY()].isEmpty() && match.getBoard()[c.getX()][c.getY()].getLevel()!=4 && w.getPosition().isNear(c) && match.getBoard()[c.getX()][c.getY()].level_diff(match.getBoard()[w.getPosition().getX()][w.getPosition().getY()])<1){
                return true;
            }
        }
        return false;
    }

    /**
     * create an arrayList with all the coordinates in wich the worker can move
     * @param match match played
     * @param ch owner of turn
     * @param id the worker id
     * @param athenaOn true if athena is on
     * @return the list
     */
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Player player = match.getPlayer(ch.getName());
        for (int i = 0; i < match.getRows(); i++) {
            for (int j = 0; j < match.getColumns(); j++) {
                Coordinate c = new Coordinate(i, j);
                if (canMoveTo(match, player.getWorker(id), c,athenaOn)) {
                    coordinates.add(new Coordinate(i, j));
                }
            }
        }
        return coordinates;
    }

    /**
     * print the list of valids coordinate
     * @param coordinates
     * @return the string that print the list
     */
    public String printCoordinates(ArrayList<Coordinate> coordinates){
        String c = "Valid coordinates:\n";
        for(int i=0; i<coordinates.size(); i++){
            c = c + i + ") " + coordinates.get(i).toString() + "\n";
        }
        return c;
    }
}
