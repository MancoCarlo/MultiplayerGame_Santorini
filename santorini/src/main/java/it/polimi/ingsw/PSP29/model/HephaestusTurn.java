package it.polimi.ingsw.PSP29.model;

public class HephaestusTurn extends GodTurn {
    public HephaestusTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p) {
        return super.winCondition(m, p);
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()!=4 || m.getBoard()[c.getX()][c.getY()].isEmpty()){
            m.updateBuilding(c);
            w.changeBuilt();
            if(m.getBoard()[c.getX()][c.getY()].getLevel()<3)//non posso costruire una cupola
                m.updateBuilding(c);
            return true;
        }
        return false;
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        return super.move(m, w, c);
    }

    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        return super.limited_move(m, w, c);
    }

    @Override
    public boolean cantMove(Match match, Worker w, boolean athena) { return super.cantMove(match, w, athena); }
}
