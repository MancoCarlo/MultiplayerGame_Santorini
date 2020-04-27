package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HephaestusTurnTest {
    Match m = null;
    HephaestusTurn turn = null;

    @Before
    public void setUp() {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21));
        m.getPlayers().add(new Player("Carlo", 21));
        turn = new HephaestusTurn(new GodTurn(new BaseTurn()));
    }
    @After
    public void TearDown(){

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
    public void build_notValidCoordinateInput_falseOutput() {
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.build(m, wL, cC));
        assertFalse(wL.getBuilt());
    }
    @Test
    public void move_notEmptyBoxInput_callSuperMove_falseOutput() {
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.move(m, wL, cC));
        assertFalse(wL.getMoved());
        assertTrue(wL.getPosition().equals(cL));
        assertFalse(m.getBoard()[cC.getX()][cC.getY()].isEmpty());
    }
    @Test
    public void limited_move_upperLevelBoxInput_callSuperLimitedMove_falseOutput() {
        Coordinate c = new Coordinate(1,1);
        Coordinate c1 = new Coordinate(1,2);
        m.updateBuilding(c1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertTrue(m.getBoard()[c1.getX()][c1.getY()].level_diff(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()])>0);
        assertFalse(turn.limited_move(m, wL, c1));
        assertFalse(wL.getMoved());
        assertTrue(wL.getPosition().equals(c));
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
        assertTrue(turn.move(m, wL, cnext));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(cnext));
        assertTrue(m.getBoard()[c.getX()][c.getY()].isEmpty());
        assertFalse(m.getBoard()[cnext.getX()][cnext.getY()].level_diff(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()])>0);
    }
    @Test
    public void cantMove_correctInputs_callSuperCantMove_falseOutput() {
        Coordinate c = new Coordinate(1,1);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c);
        assertFalse(turn.cantMove(m, wL, false));
        assertFalse(turn.cantMove(m, wL, true));
    }
}
