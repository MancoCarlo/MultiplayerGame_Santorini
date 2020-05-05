package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class HephaestusTurn extends GodTurn {

    public HephaestusTurn(Turn turn) {
        super(turn);
    }


    /**
     * allows w to build two times but it has to be in the same box and not a dome
     *
     * @param m match played
     * @param ch clientHandler that must build
     * @param server manage the interaction with client
     * @return true if w has built at least once, else false
     */

    @Override
    public boolean build(Match m, ClientHandler ch, Server server) {
        int wID = 2;
        Player p = m.getPlayer(ch.getName());
        if (p.getWorker(0).getMoved()) wID = 0;
        if (p.getWorker(1).getMoved()) wID = 1;
        ArrayList<Coordinate> coordinates = whereCanBuild(m, ch, wID);
        server.write(ch, "serviceMessage", "Build: ");
        if (coordinates.size() != 0) {
            Coordinate c = null;
            server.write(ch, "serviceMessage", printCoordinates(coordinates));
            server.write(ch, "interactionServer", "Where you want to build?\n");
            int id;
            while (true) {
                try {
                    id = Integer.parseInt(server.read(ch));
                    if (id < 0 || id >= coordinates.size()) {
                        server.write(ch, "serviceMessage", "Invalid input\n");
                        server.write(ch, "interactionServer", "Try another index: ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    server.write(ch, "serviceMessage", "Invalid input\n");
                    server.write(ch, "interactionServer", "Try another index: ");
                }
            }
            c = coordinates.get(id);
            m.updateBuilding(c);
            m.getBoard()[c.getX()][c.getY()].setLevelledUp(true);
            p.getWorker(wID).changeBuilt(true);
            String power;
            if (m.getBoard()[c.getX()][c.getY()].getLevel() < 3) {
                server.write(ch, "interactionServer", "Would you like to build another block on your previous one?\n1) Yes\n2) No\n");
                power = server.read(ch);
                if (power.equals("1"))
                    m.updateBuilding(c);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    @Override
    public boolean move(Match m, ClientHandler ch, Server server, boolean athenaOn) {
        return super.move(m, ch, server, athenaOn);
    }

    @Override
    public boolean canMoveTo(Match m,Worker w,Coordinate c, boolean athena){ return super.canMoveTo(m,w,c,athena);
    }

    @Override
    public boolean canBuildIn(Match match,Worker w,Coordinate c){ return super.canBuildIn(match,w,c);}

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
    }

    @Override
    public ArrayList<Coordinate> whereCanBuild(Match match, ClientHandler ch, int id){ return super.whereCanBuild(match, ch,id);}

    @Override
    public String printCoordinates(ArrayList<Coordinate> coordinates) {
        return super.printCoordinates(coordinates);
    }


}