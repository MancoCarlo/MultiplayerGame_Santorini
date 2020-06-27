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
        ArrayList<Coordinate> coordinates = whereCanBuild(m, ch, wID, 2);
        if(coordinates.size()!=0) {
            server.write(ch, "serviceMessage", "BORD-" + m.printBoard());
            server.write(ch, "serviceMessage", "MSGE-You can use Hestia power\n");
            server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
            server.write(ch, "interactionServer", "INDX-Would you like to build again but not in a border box? ");
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
                    server.write(ch, "interactionServer", "INDX-Would you like to build again but not in a border box? ");
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


            if (answer.equals("1")) {
                Coordinate c1 = null;
                server.write(ch, "serviceMessage", "MSGE-Hestia's power activated \n");
                server.write(ch, "serviceMessage", "LIST-" + printCoordinates(coordinates));
                server.write(ch, "interactionServer", "TURN-Where you want to build?\n");
                int id;
                while (true) {
                    String msg = server.read(ch);
                    if (msg == null) {
                        for(ClientHandler chl : server.getClientHandlers()){
                            server.write(chl, "serviceMessage", "WINM-Player disconnected\n");
                        }
                        ch.resetConnected();
                        ch.closeConnection();
                        return false;
                    } else {
                        id = Integer.parseInt(msg);
                    }
                    if (id < 0 || id >= coordinates.size()) {
                        server.write(ch, "serviceMessage", "MSGE-Invalid input\n");
                        server.write(ch, "interactionServer", "TURN-Try another index: ");
                        continue;
                    }
                    break;
                }
                c1 = coordinates.get(id);
                m.updateBuilding(c1);
                return true;
            }
        }
        return nopower;
    }

    /**
     * create and arrayList of box where the player can build
     * @param match game played
     * @param ch client handler that must build
     * @param id id of the worker chosen
     * @param n number of times that player can build
     * @return arrayList of box
     */
    public ArrayList<Coordinate> whereCanBuild(Match match, ClientHandler ch, int id, int n) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Player player = match.getPlayer(ch.getName());
        for (int i = 0; i < match.getRows(); i++) {
            for (int j = 0; j < match.getColumns(); j++) {
                Coordinate c = new Coordinate(i, j);
                if (canBuildIn(match, player.getWorker(id), c, n)) {
                    coordinates.add(new Coordinate(i, j));
                }
            }
        }
        return coordinates;
    }

    /**
     *
     * @param match game played
     * @param w worker of the player
     * @param c coordinate where worker should build
     * @param n number of times that player can build
     * @return true if the worker can build in c
     */
    public boolean canBuildIn(Match match,Worker w,Coordinate c, int n){
        if(n==1){
            return super.canBuildIn(match, w, c);
        }
        else {
            if(!w.getPosition().isNear(c) || match.getBoard()[c.getX()][c.getY()].getLevel()==4 || !match.getBoard()[c.getX()][c.getY()].isEmpty() || c.getY()==match.getColumns()-1 || c.getY()==0 || c.getX()==match.getRows()-1 || c.getX()==0){
                return false;
            }
            else{
                return true;
            }
        }
    }
}
