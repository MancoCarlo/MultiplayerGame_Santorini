package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.Controller.ClientHandlerTest;
import it.polimi.ingsw.PSP29.Controller.Turn.BaseTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.GodTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.MinotaurTurn;
import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

public class MinotaurTurnTest {

    Match m = null;
    MinotaurTurn turn = null;
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
        Coordinate c2 = new Coordinate(0,1);
        Coordinate c3 = new Coordinate(1,1);
        Coordinate c4 = new Coordinate(1,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.updateMovement(m.getPlayer("Luca"), 1, c2);
        m.updateMovement(m.getPlayer("Letizia"), 0, c3);
        m.updateMovement(m.getPlayer("Letizia"), 1, c4);

        turn = new MinotaurTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void move_correctMoveWithPowerON() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/minotaurTurn/minotaurTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(1,0)));
        assertTrue(m.getPlayer("Letizia").getWorker(1).getPosition().equals(new Coordinate(2,0)));
    }

    @Test
    public void move_correctMoveWithPowerOFF() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/minotaurTurn/minotaurTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(0,2)));
    }

    @Test
    public void move_correctMoveWithPower_AthenaON() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/minotaurTurn/minotaurTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        m.getBoard()[1][0].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        turn.move(m, ch, server, true);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(1,1)));
        assertTrue(m.getPlayer("Letizia").getWorker(0).getPosition().equals(new Coordinate(2,2)));
    }


    @Test
    public void move_WithDisconnection1() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/minotaurTurn/minotaurTest4");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection2() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/minotaurTurn/minotaurTest5");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_WithDisconnection3() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/minotaurTurn/minotaurTest6");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }
}
