package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinotaurTurnTest {
    Match m = null;
    MinotaurTurn turn = null;
    @Before
    public void setUp() {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21));
        m.getPlayers().add(new Player("Carlo", 21));
        turn = new MinotaurTurn(new GodTurn(new BaseTurn()));
    }

    @After
    public void TearDown(){ }

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
    public void move_correctInput_trueOutput(){
        Coordinate cL = new Coordinate(1,1);
        m.updateBuilding(cL);
        Coordinate cC = new Coordinate(1,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        Coordinate cNext = wL.getPosition().nextCoordinate(m, cC);
        assertFalse(m.getBoard()[cC.getX()][cC.getY()].isEmpty() && m.getBoard()[cC.getX()][cC.getY()].level_diff(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()]) <= 1);
        assertFalse(cNext.equals(cC) || !m.getBoard()[cNext.getX()][cNext.getY()].isEmpty() || m.getBoard()[cC.getX()][cC.getY()].isEmpty() || m.getBoard()[cC.getX()][cC.getY()].getWorkerBox().getIDplayer().equals(wL.getIDplayer()) || m.getBoard()[cC.getX()][cC.getY()].level_diff(m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()])>1 || m.getBoard()[cNext.getX()][cNext.getY()].getLevel()==4);
        assertTrue(turn.move(m, wL, cC));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(cC));
        assertTrue(wC.getPosition().equals(cNext));
    }

    @Test
    public void move_notNearBoxInput_falseOutput(){
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(3,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.move(m, wL, cC));
        assertFalse(wL.getMoved());
        assertFalse(wL.getPosition().equals(cC));
    }

    @Test
    public void move_emptyNearBoxInput_trueOutput(){
        Coordinate cL = new Coordinate(1,1);
        Coordinate c = new Coordinate(0,0);
        m.updateBuilding(c);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        assertTrue(turn.move(m, wL, c));
        assertTrue(wL.getMoved());
        assertTrue(wL.getPosition().equals(c));
    }

    @Test
    public void build_notValidCoordinateInput_callSuperBuild_falseOutput() {
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.build(m, wL, cC));
        assertFalse(wL.getBuilt());
    }

    @Test
    public void cantMove_correctInputWithAthenaOn_falseOutput(){
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(1,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.cantMove(m, wL, true));
        assertTrue(wL.getPosition().isNear(m.getBoard()[cC.getX()][cC.getY()].getLocation()) && m.getBoard()[cC.getX()][cC.getY()].getLevel() != 4);
        Coordinate cnext = wL.getPosition().nextCoordinate(m, m.getBoard()[cC.getX()][cC.getY()].getLocation());
        assertTrue(!m.getBoard()[cC.getX()][cC.getY()].getLocation().equals(cnext) && m.getBoard()[cnext.getX()][cnext.getY()].isEmpty() && !m.getBoard()[cC.getX()][cC.getY()].isEmpty() && !m.getBoard()[cC.getX()][cC.getY()].getWorkerBox().getIDplayer().equals(wL.getIDplayer()) && m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()].level_diff(m.getBoard()[cC.getX()][cC.getY()]) >= 0 && m.getBoard()[cnext.getX()][cnext.getY()].getLevel() != 4);
    }
    @Test
    public void cantMove_correctInputWithAthenaOff_falseOutput(){
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(1,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertFalse(turn.cantMove(m, wL, false));
        assertTrue(wL.getPosition().isNear(m.getBoard()[cC.getX()][cC.getY()].getLocation()) && m.getBoard()[cC.getX()][cC.getY()].getLevel() != 4);
        Coordinate cnext = wL.getPosition().nextCoordinate(m, m.getBoard()[cC.getX()][cC.getY()].getLocation());
        assertTrue(!m.getBoard()[cC.getX()][cC.getY()].getLocation().equals(cnext) && m.getBoard()[cnext.getX()][cnext.getY()].isEmpty() && !m.getBoard()[cC.getX()][cC.getY()].isEmpty() && !m.getBoard()[cC.getX()][cC.getY()].getWorkerBox().getIDplayer().equals(wL.getIDplayer()) && m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()].level_diff(m.getBoard()[cC.getX()][cC.getY()]) >= -1 && m.getBoard()[cnext.getX()][cnext.getY()].getLevel() != 4);
    }

    @Test
    public void cantMove_notAvailableNearBoxWithWorker_trueOutput(){
        Coordinate cL = new Coordinate(2,2);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        assertTrue(turn.cantMove(m, wL, false));
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                if(wL.getPosition().isNear(m.getBoard()[i][j].getLocation()))
                    assertTrue( m.getBoard()[i][j].isEmpty() || m.getBoard()[i][j].getLevel() == 4);
            }
        }
        assertTrue(turn.cantMove(m, wL, true));
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                if(wL.getPosition().isNear(m.getBoard()[i][j].getLocation()))
                    assertTrue( m.getBoard()[i][j].isEmpty() || m.getBoard()[i][j].getLevel() == 4);
            }
        }
    }

    @Test
    public void cantMove_availableUpperLevelBoxWithWorkerWithAthenaOnInput_trueOutput(){
        Coordinate cL = new Coordinate(2,2);
        Coordinate cC = new Coordinate(2,1);
        m.updateBuilding(cC);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        assertTrue(turn.cantMove(m, wL, true));
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                if(wL.getPosition().isNear(m.getBoard()[i][j].getLocation()))
                    assertTrue( m.getBoard()[i][j].isEmpty() || m.getBoard()[i][j].getLevel() == 4 || m.getBoard()[wL.getPosition().getX()][wL.getPosition().getY()].level_diff(m.getBoard()[i][j]) >= -1);
            }
        }
    }

    @Test
    public void cantMove_correctInputWithAthenaOff_notAvailableNextBox_trueOutput(){
        Coordinate cL = new Coordinate(1,1);
        Coordinate cC = new Coordinate(0,0);
        m.updateBuilding(cC);
        Worker wL = m.getPlayers().get(0).getWorker(0);
        Worker wC = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, cL);
        m.updateMovement(m.getPlayers().get(1), 0, cC);
        Coordinate cNext = wL.getPosition().nextCoordinate(m, m.getBoard()[cC.getX()][cC.getY()].getLocation());
        assertTrue(m.getBoard()[cC.getX()][cC.getY()].getLocation().equals(cNext));
        assertTrue(turn.cantMove(m, wL, false));
    }
}
