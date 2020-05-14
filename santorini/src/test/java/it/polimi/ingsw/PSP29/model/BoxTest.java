package it.polimi.ingsw.PSP29.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoxTest {
    Box box=null;

    @Before
    public void setUp(){
        box=new Box(1, 1);
    }

    @After
    public void tearDown(){}

    @Test
    public void TestgetLevel_and_upgradeLevel(){
        assertEquals(box.getLevel(), 0);
        box.upgradeLevel();
        assertEquals(box.getLevel(), 1);
        for(int i=2; i<5; i++){
            box.upgradeLevel();
        }
        assertEquals(box.getLevel(), 4);
        box.upgradeLevel();
    }

    @Test
    public void TestgetCoordinate(){
        assertEquals(box.getLocation().getX(), 1);
        assertEquals(box.getLocation().getY(), 1);
    }

    @Test
    public void TestisEmpty(){
        assertTrue(box.isEmpty());
        box.changeState();
        assertFalse(box.isEmpty());
        box.changeState();
        assertTrue(box.isEmpty());
    }

    @Test
    public void TestgetWorker(){
        Worker w1 = new Worker(1,"paolo");
        box.setWorkerBox(w1);
        assertEquals(box.getWorkerBox(), w1);
    }

    @Test
    public void Testlevel_diff(){
        Box b = new Box(2,3);
        b.upgradeLevel();
        assertEquals(b.level_diff(box), 1);

    }
    @Test
    public void TestsetLevelledUp_and_getLevelledUp(){
        Box b = new Box(2,3);
        assertFalse(b.getlevelledUp());
        b.setLevelledUp(true);
        assertTrue(b.getlevelledUp());
    }

    @Test
    public void TestprintEmpty_emptyBoxInput(){
        Match m = new Match();
        m.inizializeBoard();
        m.getBoard()[1][4].upgradeLevel();
        m.getBoard()[1][4].upgradeLevel();
        assertEquals(m.getBoard()[1][4].printEmpty(m), "02");
    }

    @Test
    public void TestprintEmpty_notEmptyBoxInput(){
        Match m = new Match();
        m.inizializeBoard();
        Player p = new Player("mattia", 16, 1);
        m.addPlayer(p);
        Worker w = new Worker(1,"mattia");
        Coordinate c = new Coordinate (1,4);
        p.putWorker(1, m.getBoard(), c);
        m.getBoard()[1][4].upgradeLevel();
        m.getBoard()[1][4].upgradeLevel();
        m.getBoard()[1][4].upgradeLevel();
        assertEquals(m.getBoard()[1][4].printEmpty(m), "13");
    }
}
