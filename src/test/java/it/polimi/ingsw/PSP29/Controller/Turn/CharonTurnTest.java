package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.Controller.ClientHandlerTest;
import it.polimi.ingsw.PSP29.Controller.Turn.BaseTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.CharonTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.GodTurn;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

public class CharonTurnTest {

    Match m = null;
    CharonTurn turn = null;
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
        Coordinate c2 = new Coordinate(4,4);
        Coordinate c3 = new Coordinate(2,2);
        Coordinate c4 = new Coordinate(3,3);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.updateMovement(m.getPlayer("Luca"), 1, c2);
        m.updateMovement(m.getPlayer("Letizia"), 0, c3);
        m.updateMovement(m.getPlayer("Letizia"), 1, c4);

        turn = new CharonTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void move_correctWithPowerON() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,1)));
        assertTrue(m.getPlayer("Luca").getWorker(0).getMoved());
        assertTrue(m.getPlayer("Letizia").getWorker(0).getPosition().equals(new Coordinate(0,0)));
    }

    @Test
    public void move_correctWithPowerOFF1() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(m.getPlayer("Luca").getWorker(0).getMoved());
        assertTrue(m.getPlayer("Letizia").getWorker(0).getPosition().equals(new Coordinate(2,2)));
    }

    @Test
    public void move_correctWithPowerOFF2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest2B");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(3,4)));
        assertTrue(m.getPlayer("Luca").getWorker(1).getMoved());
    }

    @Test
    public void move_WithDisconnection1() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest4");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection3() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest5");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection4() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest6");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection5() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest7");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection6() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest8");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection7() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/charonTurn/charonTest9");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

}
