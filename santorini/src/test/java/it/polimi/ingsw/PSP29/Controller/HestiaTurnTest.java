package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class HestiaTurnTest {
    Match m = null;
    HestiaTurn turn = null;
    Server server = null;
    ClientHandler ch = null;

    @Before
    public void setUp(){
        m = new Match();
        server = new Server();
        m.inizializeBoard();
        m.getPlayers().add(new Player("Luca", 21, 1));
        m.getPlayers().add(new Player("Letizia", 21, 2));
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(2,2);
        Coordinate c3 = new Coordinate(3,3);
        Coordinate c4 = new Coordinate(4,4);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.updateMovement(m.getPlayer("Luca"), 1, c2);
        m.updateMovement(m.getPlayer("Letizia"), 0, c3);
        m.updateMovement(m.getPlayer("Letizia"), 1, c4);

        turn = new HestiaTurn(new GodTurn(new BaseTurn()));
    }


    @Test
    public void build_correctWithPowerON(){
        ch = new ClientHandlerTest("src/test/resources/hestiaTurn/hestiaTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        turn.build(m, ch, server);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(1,1)));
        assertTrue(m.getBoard()[0][1].getlevelledUp());
        assertEquals(m.getBoard()[0][1].getLevel(), 1);
        assertEquals(m.getBoard()[1][2].getLevel(), 1);
    }

    @Test
    public void build_NOcorrect1(){
        ch = new ClientHandlerTest("src/test/resources/hestiaTurn/hestiaTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertFalse(turn.build(m, ch, server));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(1,1)));
    }

    @Test
    public void build_SecondBuild(){
        ch = new ClientHandlerTest("src/test/resources/hestiaTurn/hestiaTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertFalse(turn.build(m, ch, server));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(1,1)));
    }

    @Test
    public void build_disconnectionYesNO(){
        ch = new ClientHandlerTest("src/test/resources/hestiaTurn/hestiaTest4");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertFalse(turn.build(m, ch, server));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(1,1)));
    }
}
