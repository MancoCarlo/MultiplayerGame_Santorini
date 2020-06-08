package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.Coordinate;
import it.polimi.ingsw.PSP29.model.Match;
import it.polimi.ingsw.PSP29.model.Player;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrometheusTurnTest {
    Match m = null;
    PrometheusTurn turn = null;
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

        turn = new PrometheusTurn(new GodTurn(new BaseTurn()));
    }

    @Test
    public void move_correctBuildMoveWorker1(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest1");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
    }

    @Test
    public void move_correctWorker2(){
        Coordinate c1 = new Coordinate(0,1);
        m.updateMovement(m.getPlayer("Luca"), 1, c1);
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest7");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, false);
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(0,0)));
    }

    @Test
    public void move_disconnectionMovementSecondWorker(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest8");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_disconnectionChooseWorker(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest2");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_disconnectionYesNoAdditionalBuild(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_disconnectionMovement(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest4");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_disconnectionAdditionalBuild(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest5");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_disconnectionYESNOAdditionalBuildWhile(){
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest6");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, false));
    }

    @Test
    public void move_worker1NoMove(){
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
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest10");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, true);
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(0,0)));
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(2,1)));
    }

    @Test
    public void move_worker2NoMove(){
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
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest10");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        turn.move(m, ch, server, true);
        assertTrue(m.getPlayer("Luca").getWorker(1).getPosition().equals(new Coordinate(0,0)));
        assertTrue(m.getPlayer("Luca").getWorker(0).getPosition().equals(new Coordinate(2,1)));
    }

    @Test
    public void move_workersNoMove(){
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
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest9");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, true));
    }

    @Test
    public void move_powerOnOnlyOneCellFreeForMovementAndBuild(){
        Coordinate c1 = new Coordinate(0,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.getBoard()[0][0].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest11");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertTrue(turn.move(m, ch, server, true));
    }

    @Test
    public void move_powerOffOnlyOneCellFreeForMovementAndBuild(){
        Coordinate c1 = new Coordinate(0,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest11");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertTrue(turn.move(m, ch, server, true));
    }

    @Test
    public void move_disconnectionOneCellFreeForMovement(){
        Coordinate c1 = new Coordinate(0,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest12");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, true));
    }

    @Test
    public void move_disconnectionYesNoRequestOneCellFreeForMovement(){
        Coordinate c1 = new Coordinate(0,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest3");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, true));
    }

    @Test
    public void move_disconnectionYesNoRequestOneCellFreeForMovementInWhile(){
        Coordinate c1 = new Coordinate(0,0);
        m.updateMovement(m.getPlayer("Luca"), 0, c1);
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[1][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[0][1].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][0].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        m.getBoard()[2][1].upgradeLevel();
        ch = new ClientHandlerTest("src/test/resources/prometheusTurn/prometheusTest6");
        ch.setName("Luca");
        server.getClientHandlers().add(ch);
        assertFalse(turn.move(m, ch, server, true));
    }

}
