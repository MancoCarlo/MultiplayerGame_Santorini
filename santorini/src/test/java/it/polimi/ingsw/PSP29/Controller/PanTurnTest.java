package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PanTurnTest {
    Match m = null;
    PanTurn turn = null;

    @Before
    public void setUp() {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21));
        m.getPlayers().add(new Player("Carlo", 21));
        turn = new PanTurn(new GodTurn(new BaseTurn()));
    }

    @After
    public void TearDown() { }

    @Test
    public void winCondition_notValidPreviousPositionInput_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate cnext = new Coordinate(1,2);
        Coordinate cbuild = new Coordinate(1,3);
        m.updateBuilding(c);
        m.updateBuilding(c);
        m.updateBuilding(c);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        m.updateBuilding(cnext);
        m.updateBuilding(cnext);
        m.updateBuilding(cnext);
        if(turn.move(m, wL, cnext) && turn.build(m, wL, cbuild))
        {
            assertFalse(turn.winCondition(m,m.getPlayers().get(0)));
        }
    }

    @Test
    public void winCondition_validWinningConditionsInput_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate cnext = new Coordinate(1,2);
        Coordinate cbuild = new Coordinate(1,3);
        m.updateBuilding(c);
        m.updateBuilding(c);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        m.updateBuilding(cnext);
        m.updateBuilding(cnext);
        m.updateBuilding(cnext);
        if(turn.move(m, wL, cnext) && turn.build(m, wL, cbuild))
        {
            assertTrue(turn.winCondition(m,m.getPlayers().get(0)));
            assertTrue(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()].getLevel() == 3 && m.getBoard()[wL.getPrev_position().getX()][wL.getPrev_position().getY()].getLevel() == 2);
        }
    }

    @Test
    public void winCondition_validWinningConditionsWithSameLevelPrevPositionInput_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate cnext = new Coordinate(1,2);
        m.updateBuilding(c);
        m.updateBuilding(c);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        m.updateBuilding(cnext);
        m.updateBuilding(cnext);
        m.updateBuilding(cnext);
        if(turn.move(m, wL, cnext) && turn.build(m, wL, c))
        {
            assertTrue(turn.winCondition(m,m.getPlayers().get(0)));
            assertTrue(m.getBoard()[wL.getPrev_position().getX()][wL.getPrev_position().getY()].getlevelledUp() && m.getBoard()[wL.getPrev_position().getX()][wL.getPrev_position().getY()].getLevel()==3);
        }
    }

    @Test
    public void winCondition_notValidTurnInput_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        wL.changeBuilt(false);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertFalse(turn.winCondition(m,m.getPlayers().get(0)));
        assertFalse(wL.getMoved() && wL.getBuilt());
    }

    @Test
    public void winCondition_notValidLevelInput_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        wL.changeMoved(true);
        wL.changeBuilt(true);
        assertFalse(turn.winCondition(m,m.getPlayers().get(0)));
    }

    @Test
    public void build_correctInput_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(1,0);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(turn.build(m, wL, c1));
        assertTrue(wL.getBuilt());
        assertEquals(m.getBoard()[c1.getX()][c1.getY()].getLevel(),1);
    }

    @Test
    public void move_correctInput_callSuperMove_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate cnext = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(turn.move(m, wL, cnext));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(cnext));
        assertTrue(m.getBoard()[c.getX()][c.getY()].isEmpty());
    }

    @Test
    public void limited_move_correctInput_callSuperLimitedMove_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        m.updateBuilding(c);
        Coordinate cnext = new Coordinate(0,0);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(turn.limited_move(m, wL, cnext));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(cnext));
        assertTrue(m.getBoard()[c.getX()][c.getY()].isEmpty());
        assertFalse(m.getBoard()[cnext.getX()][cnext.getY()].level_diff(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()])>0);
    }

    @Test
    public void cantMove_correctInput_falseOutput() {
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertFalse(turn.cantMove(m, wL, false));
        assertFalse(turn.cantMove(m, wL, true));
    }
}