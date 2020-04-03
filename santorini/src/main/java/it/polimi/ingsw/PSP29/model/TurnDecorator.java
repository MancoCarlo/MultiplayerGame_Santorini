package it.polimi.ingsw.PSP29.model;

public abstract class TurnDecorator implements Turn {

    private final Turn turn;

    protected TurnDecorator(Turn turn){ this.turn=turn; }

    @Override
    public boolean winCondition(Match m, Player p) {
        for(Worker w : p.getWorkers()){
            if(w.getMoved() && w.getBuilt()){
                w.changeMoved();
                w.changeBuilt();
                if(m.getBoard()[w.getPosition().getX()][w.getPosition().getX()].getLevel()==3 && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getX()].getLevel()==2){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()==4){
            return false;
        }
        else{
            m.updateBuilding(c);
            w.changeBuilt();
            return true;
        }
    }

    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved();
            return true;
        }
    }

    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved();
            return true;
        }
    }
}
