package it.polimi.ingsw.PSP29.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtlasTurnTest {
    Match m = null;
    AtlasTurn turn = null;

    @Before
    public void setUp() throws Exception {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player(1,"Luca", 21));
        m.getPlayers().add(new Player(2,"Letizia", 21));
        turn = new AtlasTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void build_CorrectInputs_ReturnTrue() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        assertTrue(turn.build(m, w1,c2));
        assertTrue(w1.getBuilt());
        assertTrue(m.getBoard()[c2.getX()][c2.getY()].getLevel() == 4);
    }

    @Test
    public void build_CorrectInputs_ReturnFalse() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        assertFalse(turn.build(m, w1,c2));
        assertFalse(w1.getBuilt());
    }

    @Test
    public void methodsSuper_CorrectInputs_ReturnTrue(){
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Coordinate c3 = new Coordinate(1,3);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        assertTrue(turn.move(m,w1,c2));
        assertTrue(turn.limited_move(m,w1,c3));
        assertFalse(turn.winCondition(m,m.getPlayers().get(0)));
    }
}
