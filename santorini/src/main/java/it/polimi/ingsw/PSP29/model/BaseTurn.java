package it.polimi.ingsw.PSP29.model;

public class BaseTurn implements Turn {

    @Override
    public boolean winCondition(Match m, Player p) {
        for(Worker w : p.getWorkers()){
            if(w.getMoved() && w.getBuilt()){
                w.changeMoved();
                w.changeBuilt();
                System.out.println("Turno completato");
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
        if(w.getPosition().equals(c) || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
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
        if(w.getPosition().equals(c) || !w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved();
            return true;
        }
    }

    @Override
    public boolean cantMove(Match match,Worker w, boolean athena){
        if(athena){
            for(int i=0; i<match.getRows(); i++){
                for(int j=0; j<match.getColumns(); j++){
                    if(match.getBoard()[i][j].isEmpty() && w.getPosition().isNear(match.getBoard()[i][j].getLocation()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[i][j])<1){
                        return false;
                    }
                }
            }
        }
        else{
            for(int i=0; i<match.getRows(); i++){
                for(int j=0; j<match.getColumns(); j++){
                    if(match.getBoard()[i][j].isEmpty() && w.getPosition().isNear(match.getBoard()[i][j].getLocation()) && match.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(match.getBoard()[i][j])<2){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
