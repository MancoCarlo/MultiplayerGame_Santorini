package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AthenaTurnTest {
    Match m = null;
    AthenaTurn turn = null;
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

        turn = new AthenaTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void move_correctMove(){
        ch = new ClientHandlerTest("src/test/resources/athenaTurn/athenaTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
    }
}
