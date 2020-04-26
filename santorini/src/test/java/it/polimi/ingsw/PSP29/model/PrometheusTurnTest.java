package it.polimi.ingsw.PSP29.model;

import it.polimi.ingsw.PSP29.controller.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrometheusTurnTest {
    Match m = null;
    PrometheusTurn turn = null;

    @Before
    public void setUp() {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21));
        m.getPlayers().add(new Player("Carlo", 21));
        turn = new PrometheusTurn(new GodTurn(new BaseTurn()));
    }
    @After
    public void TearDown(){

    }
    @Test
    public void move_notValidCoordinateInput_falseOutput() {
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.move(m, wL, cC));
        assertFalse(wL.getMoved());
    }

}
