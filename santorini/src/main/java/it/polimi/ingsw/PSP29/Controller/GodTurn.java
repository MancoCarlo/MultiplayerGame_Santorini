package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.ArrayList;

public class GodTurn extends TurnDecorator{

    public GodTurn(Turn turn) { super(turn);}

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, ClientHandler ch, Server server) {
        return super.build(m, ch, server);
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
