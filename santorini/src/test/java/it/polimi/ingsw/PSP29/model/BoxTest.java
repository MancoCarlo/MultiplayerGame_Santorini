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
}
