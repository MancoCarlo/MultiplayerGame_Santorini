package it.polimi.ingsw.PSP29.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkerTest {
    Worker worker=null;
    Match m = new Match();

    @Before
    public void setUp(){
        worker=new Worker(1, "Luca");
        m.inizializeBoard();
    }

    @After
    public void tearDown(){}

    @Test
    public void setPosition_correctInput_correctOutput(){
        Coordinate c=new Coordinate(1, 4);
        assertNull(worker.getPosition());
        assertNull(worker.getPrev_position());
        worker.setPosition(c);
        assertEquals(worker.getPosition(), c);
    }

    @Test
    public void getID_correctOutput(){
        assertEquals(worker.getID(), 1);
        worker.setID(2);
        assertEquals(worker.getID(), 2);
    }

    @Test
    public void getIDplayer_correctOutput(){
        assertEquals(worker.getIDplayer(), "Luca");
        worker.setIDplayer("Lucas");
        assertEquals(worker.getIDplayer(), "Lucas");
    }

    @Test
    public void canBuild_returnTrue(){
        Coordinate c1 = new Coordinate(1,1);
        worker.setPosition(c1);
        assertTrue(worker.canBuild(m));
    }

    @Test
    public void canBuild_returnFalse(){
        Coordinate c1 = new Coordinate(1,1);
        worker.setPosition(c1);
        for(int i = 0;i<m.getRows();i++){
            for(int j = 0; j<m.getColumns(); j++){
                if(i==c1.getX() && j==c1.getY()){

                } else{
                    m.getBoard()[i][j].upgradeLevel();
                    m.getBoard()[i][j].upgradeLevel();
                    m.getBoard()[i][j].upgradeLevel();
                    m.getBoard()[i][j].upgradeLevel();
                }
            }
        }
        assertFalse(worker.canBuild(m));
    }

    @Test
    public void canLevelUp_returnFalse(){
        Coordinate c1 = new Coordinate(0,0);
        worker.setPosition(c1);
        for(int i = 0;i<m.getRows();i++){
            for(int j = 0; j<m.getColumns(); j++){
                if (i != c1.getX() && j != c1.getY()) {
                    m.getBoard()[i][j].upgradeLevel();
                    m.getBoard()[i][j].upgradeLevel();
                    m.getBoard()[i][j].upgradeLevel();
                    m.getBoard()[i][j].upgradeLevel();
                }
            }
        }
        assertFalse(worker.canLevelUp(m));
    }

    @Test
    public void canLevelUp_returnTrue(){
        Coordinate c1 = new Coordinate(4,4);
        worker.setPosition(c1);
        for(int i = 0;i<m.getRows();i++){
            for(int j = 0; j<m.getColumns(); j++){
                if(i==c1.getX() && j==c1.getY()){

                } else{
                    m.getBoard()[i][j].upgradeLevel();
                }
            }
        }
        assertTrue(worker.canLevelUp(m));
    }

    @Test
    public void changeBuilt_getBuilt_correctOutput(){
        worker.setBuilt(true);
        assertTrue(worker.getBuilt());
        worker.changeBuilt(false);
        assertFalse(worker.getBuilt());
    }

    @Test
    public void changeMoved_getMoved_correctOutput(){
        worker.setMoved(true);
        assertTrue(worker.getMoved());
        worker.changeMoved(false);
        assertFalse(worker.getMoved());
    }

}
