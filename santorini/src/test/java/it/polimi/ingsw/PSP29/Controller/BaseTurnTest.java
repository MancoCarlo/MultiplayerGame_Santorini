package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class BaseTurnTest {

    Match m = null;
    BaseTurn turn = null;
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

        turn = new BaseTurn();
    }

    @Test
    public void moveAndBuild_correct() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/baseTurn/baseTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertTrue(turn.move(m, ch, server, false));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(turn.build(m, ch, server));
        assertEquals(m.getBoard()[0][1].getLevel(), 1);
        assertFalse(turn.winCondition(m, m.getPlayer("Luca")));
    }

    @Test
    public void moveAndBuild_correct_OneWorkerBlocked() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/baseTurn/baseTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);

        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][2].upgradeLevel();
        m.getBoard()[0][2].upgradeLevel();
        m.getBoard()[1][0].upgradeLevel();
        m.getBoard()[1][0].upgradeLevel();
        m.getBoard()[1][2].upgradeLevel();
        m.getBoard()[1][2].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();

        assertTrue(turn.move(m, ch, server, false));
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(1,3)));
        assertTrue(turn.build(m, ch, server));
        assertEquals(m.getBoard()[0][2].getLevel(), 3);
        assertFalse(turn.winCondition(m, m.getPlayer("Luca")));
    }

    @Test
    public void move_with_disconnection1() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/baseTurn/baseTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_with_disconnection2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/baseTurn/baseTest4");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void build_with_disconnection1() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/baseTurn/baseTest5");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertFalse(turn.build(m, ch, server));
    }
}
