package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseTurnTest {
    Match m = null;
    BaseTurn turn = null;

    @Before
    public void setUp() {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21));
        m.getPlayers().add(new Player("Carlo", 21));
        turn = new BaseTurn();
    }
    @After
    public void TearDown(){

    }
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
        }
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
    public void winCondition_notValidTurnInput_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        wL.changeBuilt(false);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertFalse(turn.winCondition(m,m.getPlayers().get(0)));
    }
    @Test
    public void move_notValidNearBoxInput_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(0,4);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertFalse(c.isNear(c1));
        assertFalse(turn.move(m, wL, c1));
        assertFalse(wL.getMoved());
        assertTrue(wL.getPosition().equals(c));
    }
    @Test
    public void move_correctInput_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(turn.move(m, wL, c1));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(c1));
    }
    @Test
    public void limited_move_upperLevelBoxInput_falseOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(0,2);
        m.updateBuilding(c1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(m.getBoard()[c1.getX()][c1.getY()].level_diff(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()])>0);
        assertFalse(turn.limited_move(m, wL, c1));
        assertFalse(wL.getMoved());
        assertTrue(wL.getPosition().equals(c));
    }
    @Test
    public void limited_move_correctInput_trueOutput(){
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(0,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(turn.limited_move(m, wL, c1));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(c1));
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
    public void build_notEmptyBoxInput_falseOutput(){
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.build(m, wL, cC));
        assertFalse(wL.getBuilt());
        assertEquals(m.getBoard()[cC.getX()][cC.getY()].getLevel(),0);
    }
    @Test
    public void cantMove_correctInput_falseOutput() {
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertFalse(turn.cantMove(m, wL, false));
        assertFalse(turn.cantMove(m, wL, true));
    }
    @Test
    public void cantMove_notValidInputWithAthenaOn_trueOutput() {
        Coordinate c = new Coordinate(0,0);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        m.updateBuilding(new Coordinate(0,1));
        m.updateBuilding(new Coordinate(1,1));
        m.updateBuilding(new Coordinate(1,0));
        assertTrue(turn.cantMove(m, wL, true));
        assertTrue(wL.canLevelUp(m));
    }
    @Test
    public void cantMove_notValidInputWithAthenaOff_trueOutput() {
        Coordinate c = new Coordinate(0,0);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        m.updateBuilding(new Coordinate(0,1));
        m.updateBuilding(new Coordinate(0,1));
        m.updateBuilding(new Coordinate(1,0));
        m.updateBuilding(new Coordinate(1,0));
        m.updateBuilding(new Coordinate(1,1));
        m.updateBuilding(new Coordinate(1,1));
        assertTrue(turn.cantMove(m, wL, false));
        assertFalse(wL.canLevelUp(m));
    }
}
