package it.polimi.ingsw.PSP29;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkerTest {
    Worker worker=null;

    @Before
    public void setUp(){
        worker=new Worker(1, "Luca");
    }

    @After
    public void tearDown(){}

    @Test
    public void setPosition_correctInput_correctOutput(){
        Coordinate c=new Coordinate(1, 4);
        assertNull(worker.getPosition());
        worker.setPosition(c);
        assertEquals(worker.getPosition(), c);
    }

    @Test
    public void getID_correctOutput(){
        assertEquals(worker.getID(), 1);
    }

    @Test
    public void getIDplayer_correctOutput(){
        assertEquals(worker.getIDplayer(), "Luca");
    }
}
