package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.model.Worker;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ApolloTurnTest {
    Match m = null;
    ApolloTurn turn = null;
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

        turn = new ApolloTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void move_correctMoveWithSwap() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/apolloTurn/apolloTest1");
        ch.setName("Luca");
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(3,3)));
    }

    @Test
    public void move_correctMove() throws FileNotFoundException {
        ch = new ClientHandlerTest("src/test/resources/apolloTurn/apolloTest2");
        ch.setName("Luca");
        turn.move(m, ch, server, true);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
    }

    @Test
    public void move_worker1noMove() throws FileNotFoundException {
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(1,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.updateMovement(m.getPlayer("Luca"), 1, c2);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/apolloTurn/apolloTest3");
        ch.setName("Luca");
        turn.move(m, ch, server, true);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(2,1)));
    }

    @Test
    public void move_worker2noMove() throws FileNotFoundException {
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(1,0);
        m.updateMovement(m.getPlayer("Luca"), 1, c1);
        m.updateMovement(m.getPlayer("Luca"), 0, c2);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/apolloTurn/apolloTest3");
        ch.setName("Luca");
        turn.move(m, ch, server, true);
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(0,0)));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(2,1)));
    }

    @Test
    public void move_workersNoMove() throws FileNotFoundException {
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(1,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.updateMovement(m.getPlayer("Luca"), 1, c2);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/apolloTurn/apolloTest3");
        ch.setName("Luca");
        assertFalse(turn.move(m, ch, server, true));
    }
}
