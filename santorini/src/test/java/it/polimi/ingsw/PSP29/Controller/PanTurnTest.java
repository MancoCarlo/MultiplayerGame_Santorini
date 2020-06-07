package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

public class PanTurnTest {

    Match m = null;
    PanTurn turn = null;
    Server server = null;
    ClientHandler ch = null;

    @Before
    public void setUp(){
        m = new Match();
        server = new Server();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21, 1));
        m.getPlayers().add(new Player("Letizia", 21, 2));
        Coordinate c1 = new Coordinate(1,1);
        Coordinate c2 = new Coordinate(2,2);
        Coordinate c3 = new Coordinate(3,3);
        Coordinate c4 = new Coordinate(4,4);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.updateMovement(m.getPlayer("Luca"), 1, c2);
        m.updateMovement(m.getPlayer("Letizia"), 0, c3);
        m.updateMovement(m.getPlayer("Letizia"), 1, c4);

        turn = new PanTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void win_correctWithPowerON() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/panTurn/panTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        assertEquals(m.getBoard()[1][1].getLevel(), 2);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(turn.winCondition(m, m.getPlayer("Luca")));
    }

    @Test
    public void win_correctWithPowerON2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/panTurn/panTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].setLevelledUp(true);
        assertEquals(m.getBoard()[1][1].getLevel(), 3);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(turn.winCondition(m, m.getPlayer("Luca")));
    }

    @Test
    public void win_correctWithPowerOFF() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/panTurn/panTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        assertEquals(m.getBoard()[1][1].getLevel(), 2);
        assertEquals(m.getBoard()[0][0].getLevel(), 3);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(turn.winCondition(m, m.getPlayer("Luca")));
    }

    @Test
    public void win_correctWithPowerOFF2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/panTurn/panTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].setLevelledUp(true);
        assertEquals(m.getBoard()[1][1].getLevel(), 3);
        assertEquals(m.getBoard()[0][0].getLevel(), 3);
        assertTrue(m.getBoard()[1][1].getlevelledUp());
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(turn.winCondition(m, m.getPlayer("Luca")));
    }

    @Test
    public void NOwin_correctWithPowerON() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/panTurn/panTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertFalse(turn.winCondition(m, m.getPlayer("Luca")));
    }

}
