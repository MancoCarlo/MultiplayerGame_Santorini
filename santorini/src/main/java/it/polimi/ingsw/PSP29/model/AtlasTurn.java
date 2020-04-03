package it.polimi.ingsw.PSP29.model;

public class AtlasTurn extends GodTurn{

    public AtlasTurn(Turn turn) {
        super(turn);
    }

    public boolean winCondition(Match m, Player p){
        return super.winCondition(m, p);
    }

    public boolean build(Match m, Worker w, Coordinate c){
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()==4){
            return false;
        }
        else{
            while(m.getBoard()[c.getX()][c.getY()].getLevel() < 4){
                m.updateBuilding(c);
            }
            w.changeBuilt();
            return true;
        }
    }

    public boolean move(Match m, Worker w, Coordinate c) { return super.move(m, w, c); }

    public boolean limited_move(Match m, Worker w, Coordinate c){ return super.limited_move(m, w, c); }
}
