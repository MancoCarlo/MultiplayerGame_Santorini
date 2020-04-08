package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;

public class MinotaurTurn extends GodTurn {

    public MinotaurTurn(Turn turn) {
        super(turn);
    }

    /**
     *call winCondition() of the superclass
     * @param m match played
     * @param p player that plays the turn
     * @return true if p wins the game, else false
     */
    @Override
    public boolean winCondition(Match m, Player p) {
    return true;
    }

    /**
     * call build() of the superclass
     * @param m match played
     * @param w worker that must build
     * @param c location of the box where w must build
     * @return true if w has built in c, else false
     */
    @Override
    public boolean build(Match m, Worker w, Coordinate c) {
        return super.build(m, w, c);
    }

    /**
     * allows a player to move his worker in an adjacent box already occupied by a worker of another player and change that worker's position in the following box
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w occupied by a worker of another player
     * @return true if is moved in c, else false
     */
    @Override
    public boolean move(Match m, Worker w, Coordinate c) {
        if(w.getPosition().isNear(c) && m.getBoard()[c.getX()][c.getY()].getLevel()!=4){//se la casella è adiacente ma non coincidente e se la torre non è completa
            if (m.getBoard()[c.getX()][c.getY()].isEmpty() && m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) <= 1) {
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved(true);
                return true;
            }
            Coordinate c1 = w.getPosition().nextCoordinate(m, c); //ottengo la coordinata seguente oppure c
            if(c1.equals(c) || !m.getBoard()[c1.getX()][c1.getY()].isEmpty() || m.getBoard()[c.getX()][c.getY()].isEmpty() || m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer().equals(w.getIDplayer()) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>1 || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4){
                //se la casella c non ha una successiva oppure se la casella successiva non è vuota oppure se la casella indicata non contiene un operaio oppure se l'operaio nella casella è dello stesso giocatore oppure se la casella in cui mi voglio spostare è alta di più di 1 livello oppure se la casella successiva è completa
                return false;
            }
            else {
                m.updateMovement(m.getPlayer(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()), m.getBoard()[c.getX()][c.getY()].getWorkerBox().getID(), c1);//costringo il worker a spostarsi
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c); //muovo il mio worker
                w.changeMoved(true);
                return true;
            }
        }
        else
            return false;
    }

    /**
     * allows a player to move his worker without upgrading his level in an adjacent box already occupied by a worker of another player and change that worker's position in the following box
     * @param m match played
     * @param w worker that must be moved
     * @param c new position of w occupied by a worker of another player
     * @return true if is moved in c, else false
     */
    @Override
    public boolean limited_move(Match m, Worker w, Coordinate c) {
        if(w.getPosition().isNear(c) && m.getBoard()[c.getX()][c.getY()].getLevel()!=4){//se la casella è adiacente ma non coincidente e se la torre non è completa
            if (m.getBoard()[c.getX()][c.getY()].isEmpty() && m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()]) <=0) {
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c);
                w.changeMoved(true);
                return true;
            }
            Coordinate c1 = w.getPosition().nextCoordinate(m, c); //ottengo la coordinata seguente oppure c
            if(c1.equals(c) || !m.getBoard()[c1.getX()][c1.getY()].isEmpty() || m.getBoard()[c.getX()][c.getY()].isEmpty() || m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer().equals(w.getIDplayer()) || m.getBoard()[c.getX()][c.getY()].level_diff(m.getBoard()[w.getPosition().getX()][w.getPosition().getY()])>0 || m.getBoard()[c1.getX()][c1.getY()].getLevel()==4)
                //se la casella c non ha una successiva oppure se la casella successiva non è vuota oppure se la casella indicata non contiene un operaio oppure se l'operaio nella casella è dello stesso giocatore oppure se la casella in cui mi voglio spostare è piu alta della mia casella oppure se la casella successiva è completa
                return false;
            else {
                m.updateMovement(m.getPlayer(m.getBoard()[c.getX()][c.getY()].getWorkerBox().getIDplayer()), m.getBoard()[c.getX()][c.getY()].getWorkerBox().getID(), c1);//costringo il worker a spostarsi
                m.updateMovement(m.getPlayer(w.getIDplayer()), w.getID(), c); //muovo il mio worker
                w.changeMoved(true);
                return true;
            }
        }
        else
            return false;
    }

    /**
     * checks if a player can't move his worker using Minotaur power
     * @param m match played
     * @param w worker that can be moved
     * @param athena true if the athena power is on, else false
     * @return true if w can't move to another location, else false
     */
    @Override
    public boolean cantMove(Match m, Worker w, boolean athena) {
        if (athena) {
            for (int i = 0; i < m.getRows(); i++) {
                for (int j = 0; j < m.getColumns(); j++) {
                    if (w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() != 4) { //se la casella è adiacente ma non coincidente e se la torre non è completa
                        Coordinate c1 = w.getPosition().nextCoordinate(m, m.getBoard()[i][j].getLocation());//restituisce la casella stessa se non ha una successiva
                        if (!m.getBoard()[i][j].getLocation().equals(c1) && m.getBoard()[c1.getX()][c1.getY()].isEmpty() && !m.getBoard()[i][j].isEmpty() && !m.getBoard()[i][j].getWorkerBox().getIDplayer().equals(w.getIDplayer()) && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(m.getBoard()[i][j]) >= 0 && m.getBoard()[c1.getX()][c1.getY()].getLevel() != 4) {
                            //se la casella c ha una successiva e se la casella successiva  è vuota e se la casella indicata contiene un operaio e se l'operaio nella casella non è dello stesso giocatore e se la casella in cui mi voglio spostare non è piu alta del mio livello e se la casella successiva non è completa
                            return false; //si puo usare la divinità
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < m.getRows(); i++) {
                for (int j = 0; j < m.getColumns(); j++) {
                    if (w.getPosition().isNear(m.getBoard()[i][j].getLocation()) && m.getBoard()[i][j].getLevel() != 4) { //se la casella è adiacente ma non coincidente e se la torre non è completa
                        Coordinate c1 = w.getPosition().nextCoordinate(m, m.getBoard()[i][j].getLocation());//restituisce la casella stessa se non ha una successiva
                        if (!m.getBoard()[i][j].getLocation().equals(c1) && m.getBoard()[c1.getX()][c1.getY()].isEmpty() && !m.getBoard()[i][j].isEmpty() && !m.getBoard()[i][j].getWorkerBox().getIDplayer().equals(w.getIDplayer()) && m.getBoard()[w.getPosition().getX()][w.getPosition().getY()].level_diff(m.getBoard()[i][j]) >= -1 && m.getBoard()[c1.getX()][c1.getY()].getLevel() != 4) {
                            //se la casella c ha una successiva e se la casella successiva  è vuota e se la casella indicata contiene un operaio e se l'operaio nella casella è dello stesso giocatore e se la casella in cui mi voglio spostare non è alta di più di 1 livello e se la casella successiva non è completa
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    }


