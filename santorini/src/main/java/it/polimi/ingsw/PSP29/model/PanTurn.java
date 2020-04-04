package it.polimi.ingsw.PSP29.model;

public class PanTurn extends GodTurn {

    public PanTurn(Turn turn) {
        super(turn);
    }

    @Override
    public boolean winCondition(Match m, Player p) {
        for(Worker w : p.getWorkers()) {
            if (w.getMoved() && w.getBuilt()) {
                w.changeMoved();
                w.changeBuilt();
                if (m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel() == 3 && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel() == 2)
                    return true;
                if(m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) >=2)
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return super.build(m, w, c);
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
