package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApolloTurnTest {
    Match m = null;
    ApolloTurn turn = null;

    @Before
    public void setUp() throws Exception {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21));
        m.getPlayers().add(new Player("Letizia", 21));
        turn = new ApolloTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void build_CorrectInputs_callSuperBuild() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        assertTrue(turn.build(m, w1, c2));
        assertEquals(m.getBoard()[c2.getX()][c2.getY()].getLevel(),1);
        assertTrue(w1.getBuilt());
        assertFalse(turn.winCondition(m, m.getPlayers().get(0)));
    }

    @Test
    public void move_CorrectInputs_SwapWorkerReturnTrue() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        Worker w2 = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.updateMovement(m.getPlayers().get(1), 0, c2);
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        assertTrue(turn.move(m, w1,c2));
        assertTrue(w1.getPosition() == c2 && w2.getPosition() == c1);
        assertTrue(w1.getMoved());
        Coordinate c3 = new Coordinate(1,3);
        assertTrue(turn.move(m, w1,c3));
    }

    @Test
    public void move_CorrectInputs_LevelBoxTooHighReturnFalse() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        Worker w2 = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.updateMovement(m.getPlayers().get(1), 0, c2);
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        assertFalse(turn.move(m, w1,c2));
        assertFalse(w1.getPosition() == c2 && w2.getPosition() == c1);
    }

    @Test
    public void limitedMove_CorrectInputsAthenaON_LevelBoxTooHighReturnFalse() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        Worker w2 = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.updateMovement(m.getPlayers().get(1), 0, c2);
        m.getBoard()[c2.getX()][c2.getY()].upgradeLevel();
        assertFalse(turn.limited_move(m, w1,c2));
        assertFalse(w1.getPosition() == c2 && w2.getPosition() == c1);
    }

    @Test
    public void limitedMove_CorrectInputsAthenaON_SwapWorkerReturnTrue() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        Worker w2 = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.updateMovement(m.getPlayers().get(1), 0, c2);
        assertTrue(turn.limited_move(m, w1,c2));
        assertTrue(w1.getPosition() == c2 && w2.getPosition() == c1);
        assertTrue(w1.getMoved());
    }

    @Test
    public void limitedMove_CorrectInputsAthenaON_SuperLimitedMoveReturnTrue() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        assertTrue(turn.limited_move(m, w1,c2));
        assertTrue(w1.getPosition() == c2);
        assertTrue(w1.getMoved());
    }

    @Test
    public void cantmove_CorrectInputsAthenaON_ReturnTrue() {
        Coordinate c1 = new Coordinate(1,1);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        for(int i=0; i<m.getRows();i++){
            for(int j=0; j<m.getColumns();j++)
                if(i == c1.getX() && j == c1.getY()){}
                else m.getBoard()[i][j].upgradeLevel();
        }
        assertTrue(turn.cantMove(m, w1, true));
    }

    @Test
    public void cantmove_CorrectInputsAthenaOnBoxNotEmpty_ReturnFalse() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        Worker w2 = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.updateMovement(m.getPlayers().get(1), 0, c2);
        for(int i=0; i<m.getRows();i++){
            for(int j=0; j<m.getColumns();j++)
                if(i != 1 || j != 1 || j != 2)
                    m.getBoard()[i][j].upgradeLevel();
        }
        assertFalse(turn.cantMove(m, w1, true));
    }

    @Test
    public void cantmove_CorrectInputsAthenaOFFBoxNotEmpty_ReturnFalse() {
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(1,2);
        Worker w1 = m.getPlayers().get(0).getWorker(0);
        Worker w2 = m.getPlayers().get(1).getWorker(0);
        m.updateMovement(m.getPlayers().get(0), 0, c1);
        m.updateMovement(m.getPlayers().get(1), 0, c2);
        for(int i=0; i<m.getRows();i++){
            for(int j=0; j<m.getColumns();j++)
                if(i != 1 || j != 1)
                    m.getBoard()[i][j].upgradeLevel();
        }
        assertFalse(turn.cantMove(m, w1, false));
    }

}
