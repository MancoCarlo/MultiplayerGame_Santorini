package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class HestiaTurn extends GodTurn{

    public HestiaTurn(Turn turn) { super(turn);}

    /**
     * allows w to build two times but the second building can't be a border box
     * @param m match played
     * @param ch clientHandler that must build
     * @param server manage the interaction with client
     * @return true if w has built at least once
     */
    @Override
    public boolean build(Match m, ClientHandler ch, Server server) {
        Player p = m.getPlayer(ch.getName());
        int wID=2;
        boolean nopower = super.build(m,ch,server);
        if(!nopower)
            return false;
        if(p.getWorker(0).getMoved()) wID = 0;
        if(p.getWorker(1).getMoved()) wID = 1;
        ArrayList<Coordinate> coordinates = whereCanBuild(m, ch, wID);
        for (Coordinate c: coordinates) {
            if(c.getY()==m.getColumns() || c.getY()==0 || c.getX()==m.getRows() || c.getX()==0)
                coordinates.remove(c);
        }
        if(coordinates.size()!=0) {
            server.write(ch, "serviceMessage", "BORD-" + m.printBoard());
            server.write(ch, "serviceMessage", "You can use Hestia power\n");
            server.write(ch, "serviceMessage", "LIST-1)YES\n2)NO\n");
            server.write(ch, "interactionServer", "INDX-Would you like to build again but not in a border box?");
            String answer = server.read(ch);
            if (answer.equals("1")) {
                Coordinate c1 = null;
                server.write(ch, "serviceMessage", "LIST-" + printCoordinates(coordinates));
                server.write(ch, "interactionServer", "TURN-Where you want to build?\n");
                int id;
                while (true) {
                    try {
                        String msg = server.read(ch);
                        if (msg == null) {
                            ch.resetConnected();
                            ch.closeConnection();
                            return false;
                        } else {
                            id = Integer.parseInt(msg);
                        }
                        if (id < 0 || id >= coordinates.size()) {
                            server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                            server.write(ch, "interactionServer", "INDX-Try another index: ");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                        server.write(ch, "interactionServer", "INDX-Try another index: ");
                    }
                }
                c1 = coordinates.get(id);
                m.updateBuilding(c1);
                return true;
            }
        }
        return nopower;
    }

    @Override
    public ArrayList<Coordinate> whereCanMove(Match match, ClientHandler ch, int id, boolean athenaOn) {
        return super.whereCanMove(match,ch,id,athenaOn);
    }
}
