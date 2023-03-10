package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.Controller.ClientHandlerTest;
import it.polimi.ingsw.PSP29.Controller.Turn.BaseTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.DemeterTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.GodTurn;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

public class DemeterTurnTest {

    Match m = null;
    DemeterTurn turn = null;
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

        turn = new DemeterTurn(new GodTurn(new BaseTurn()));
    }


    @Test
    public void build_correctWithPowerON() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/demeterTurn/demeterTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        turn.build(m, ch, server);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(m.getBoard()[0][1].getlevelledUp());
        assertTrue(m.getBoard()[1][0].getlevelledUp());
        assertEquals(m.getBoard()[0][1].getLevel(), 1);
        assertEquals(m.getBoard()[1][0].getLevel(), 1);
    }

    @Test
    public void build_NOcorrect1() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/demeterTurn/demeterTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertFalse(turn.build(m, ch, server));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
    }

    @Test
    public void build_NOcorrect2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/demeterTurn/demeterTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertFalse(turn.build(m, ch, server));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
    }


}
