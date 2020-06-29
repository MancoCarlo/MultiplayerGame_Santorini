package it.polimi.ingsw.PSP29.Controller.Turn;

import it.polimi.ingsw.PSP29.Controller.ClientHandlerTest;
import it.polimi.ingsw.PSP29.Controller.Turn.BaseTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.GodTurn;
import it.polimi.ingsw.PSP29.Controller.Turn.TritonTurn;
import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TritonTurnTest {
    Match m = null;
    TritonTurn turn = null;
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

        turn = new TritonTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void move_correctDoubleMoveExternalCell(){
        ch = new ClientHandlerTest("src/test/resources/tritonTurn/tritonTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,1)));
    }

    @Test
    public void move_firstMovementNotSuccesful(){
        ch = new ClientHandlerTest("src/test/resources/tritonTurn/tritonTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_yesNoDisconnectionFirstTime(){
        ch = new ClientHandlerTest("src/test/resources/tritonTurn/tritonTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_yesNoDisconnectionSecondTime(){
        ch = new ClientHandlerTest("src/test/resources/tritonTurn/tritonTest4");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_secondMovementDisconnection(){
        ch = new ClientHandlerTest("src/test/resources/tritonTurn/tritonTest5");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }
}
