package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
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
    public void winCondition_notValidLevelInput_callSuperWinCondition_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        wL.changeMoved(true);
        wL.changeBuilt(true);
        assertFalse(turn.winCondition(m,m.getPlayers().get(0)));
    }

    @Test
    public void build_correctInput_callSuperBuild_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(1,0);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(turn.build(m, wL, c1));
        assertTrue(wL.getBuilt());
        assertEquals(m.getBoard()[c1.getX()][c1.getY()].getLevel(),1);
    }

    @Test
    public void move_notValidCoordinateInput_falseOutput() {
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.move(m, wL, cC));
        assertFalse(wL.getMoved());
    }

}
