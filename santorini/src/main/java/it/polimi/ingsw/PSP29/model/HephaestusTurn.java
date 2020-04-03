package it.polimi.ingsw.PSP29.model;

public class HephaestusTurn extends GodTurn {
    protected HephaestusTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()>=2){
            return false;
        }
        else{
            m.updateBuilding(c);
            m.updateBuilding(c);
            w.changeBuilt();
            return true;
        }
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        return super.move(m, w, c);
    }

    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        return super.limited_move(m, w, c);
    }
}
