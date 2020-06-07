package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameControllerTest {

    Match m = null;
    Server server = null;

    GameController gc = null;

    @Before
    public void setUp(){
        server = new Server();
        gc = new GameController(server);
        gc.getMatch().inizializeBoard();
    }

    @Test
    public void controlMovement_correctInput(){
        Player p = new Player("Luca", 21, 0);
        Coordinate c = new Coordinate(0,0);
        assertTrue(gc.controlMovement(p, 0, c));
    }

    @Test
    public void controlMovement_NOcorrectInput(){
        Player p = new Player("Luca", 21, 0);
        Coordinate c = new Coordinate(16,0);
        assertFalse(gc.controlMovement(p, 0, c));
    }

    @Test
    public void controlMovement_correctInput_boxNotEmpty(){
        Player p = new Player("Luca", 21, 0);
        Coordinate c = new Coordinate(0,0);
        gc.getMatch().getBoard()[0][0].changeState();
        assertFalse(gc.controlMovement(p, 0, c));
    }

    @Test
    public void godAssignementTest() throws FileNotFoundException {
        gc.getMatch().addPlayer(new Player("Luca", 21, 0));
        gc.getMatch().addPlayer(new Player("Carlo", 21, 1));
        ClientHandler ch1 = new ClientHandlerTest("src/test/resources/GameController/godAssignementTestA");
        ch1.setName("Luca");
        ClientHandler ch2 = new ClientHandlerTest("src/test/resources/GameController/godAssignementTestB");
        ch2.setName("Carlo");
        server.getClientHandlers().add(ch1);
        server.getClientHandlers().add(ch2);
        gc.getMatch().getPlayer("Luca").setInGame(true);
        gc.getMatch().getPlayer("Carlo").setInGame(true);
        gc.setNumPlayers(2);
        assertEquals(gc.getMatch().playersInGame(), 2);
        assertTrue(gc.godsAssignement());
        assertEquals(gc.getMatch().getPlayer("Luca").getCard().getName(), "Arthemis");
        assertEquals(gc.getMatch().getPlayer("Carlo").getCard().getName(), "Apollo");
    }

    @Test
    public void PutWorkerTest() throws FileNotFoundException {
        gc.getMatch().addPlayer(new Player("Luca", 21, 0));
        gc.getMatch().addPlayer(new Player("Carlo", 21, 1));
        ClientHandler ch1 = new ClientHandlerTest("src/test/resources/GameController/putWorkersTestA");
        ch1.setName("Luca");
        ClientHandler ch2 = new ClientHandlerTest("src/test/resources/GameController/putWorkersTestB");
        ch2.setName("Carlo");
        server.getClientHandlers().add(ch1);
        server.getClientHandlers().add(ch2);
        gc.getMatch().getPlayer("Luca").setInGame(true);
        gc.getMatch().getPlayer("Carlo").setInGame(true);
        gc.setNumPlayers(2);
        assertTrue(gc.putWorkers());
        Coordinate c1 = new Coordinate(0,0);
        assertEquals(gc.getMatch().getPlayer("Luca").getWorker(0).getPosition(), c1);
        Coordinate c2 = new Coordinate(1,1);
        assertEquals(gc.getMatch().getPlayer("Luca").getWorker(1).getPosition(), c2);
        Coordinate c3 = new Coordinate(2,2);
        assertEquals(gc.getMatch().getPlayer("Carlo").getWorker(0).getPosition(), c3);
        Coordinate c4 = new Coordinate(3,3);
        assertEquals(gc.getMatch().getPlayer("Carlo").getWorker(1).getPosition(), c4);
    }

}
