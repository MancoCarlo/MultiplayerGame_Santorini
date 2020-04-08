package it.polimi.ingsw.PSP29.model;

public class BaseTurn implements Turn {

    /**
     * control if the player win
     * @param m match played
     * @param p player that plays the turn
     * @return true if p win the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
        for(Worker w : p.getWorkers()){
            if(w.getMoved() && w.getBuilt()){
                w.changeMoved();
                w.changeBuilt();
                if(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].getLevel()==3 && m.getBoard()[w.getPrev_position().getX()][w.getPrev_position().getY()].getLevel()==2){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * let the worker build
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w can build two times
     * @return true if w has built at least once
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateBuilding(c);
            w.changeBuilt();
            return true;
        }
    }

    /**
     * move the worker
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, Worker w, Coordinate c){
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved();
            return true;
        }
    }

    /**
     * move the worker if athena has been used
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w
     * @return true if is moved in c, else false
     */
    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c){
        if(!w.getPosition().isNear(c) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c.getX()][c.getY()].getLevel()==4 || !m.getBoard()[c.getX()][c.getY()].isEmpty()){
            return false;
        }
        else{
            m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
            w.changeMoved();
            return true;
        }
    }

    /**
     * control if the worker can move
     * @param match match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
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
