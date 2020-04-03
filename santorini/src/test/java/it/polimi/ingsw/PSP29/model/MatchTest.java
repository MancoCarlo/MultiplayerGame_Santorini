package it.polimi.ingsw.PSP29.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchTest {
    Match m = null;
    @Before
    public void setUp() throws Exception {
        m = new Match();
        m.inizializeBoard();
        m.getPlayers().add(new Player(1,"Luca", 21));
        m.getPlayers().add(new Player(2,"Letizia", 21));
    }

    @Test
    public void updateMovement_ExistingInputs_UpdateBoard() {
        Coordinate c = new Coordinate(1,1);
        Box[][] b1;
        m.updateMovement(m.getPlayers().get(0), 1, c);
        b1= m.getBoard();
        assertEquals(m.getPlayers().get(0).getWorker(1).getPosition(),c);
        for(int i=0; i<m.getRows(); i++){
            for(int j=0;j<m.getColumns();j++){
                if(i == c.getX() && j==c.getY()) assertFalse(b1[i][j].isEmpty());
                else assertTrue(b1[i][j].isEmpty());
            }
        }
    }

    @Test
    public void updateBuilding_ExistingCoordinate_UpdateBoard() {
        Coordinate c = new Coordinate(1,1);
        Box[][] b1;
        m.updateBuilding(c);
        b1= m.getBoard();
        for(int i=0; i<m.getRows(); i++){
            for(int j=0;j<m.getColumns();j++){
                if(i == c.getX() && j==c.getY()) assertEquals(b1[i][j].getLevel(), 1);
                else assertEquals(b1[i][j].getLevel(), 0);
            }
        }
    }

    @Test
    public void updateBuilding_MaxLevel_NotUpdateBoard() {
        Coordinate c = new Coordinate(1,1);
        Box[][] b1;
        b1= m.getBoard();
        b1[1][1].upgradeLevel();
        b1[1][1].upgradeLevel();
        b1[1][1].upgradeLevel();
        b1[1][1].upgradeLevel();
        for(int i=0; i<m.getRows(); i++){
            for(int j=0;j<m.getColumns();j++){
                if(i == c.getX() && j==c.getY()) assertEquals(b1[i][j].getLevel(), 4);
                else assertEquals(b1[i][j].getLevel(), 0);
            }
        }
    }

    @Test
    public void removeWorkers_ExistingWorkers_UpdateBoard(){
        Coordinate c1 = new Coordinate(0,1);
        Coordinate c2 = new Coordinate(4,2);
        m.updateMovement(m.getPlayers().get(0), 0,c1);
        m.updateMovement(m.getPlayers().get(0), 1,c2);
        m.removeWorkers(m.getPlayers().get(0));
        assertEquals(m.getPlayers().get(0).getWorker(0).getPosition(),null);
        assertEquals(m.getPlayers().get(0).getWorker(1).getPosition(),null);
        for(int i=0; i<m.getRows(); i++){
            for(int j=0;j<m.getColumns();j++){
                assertTrue(m.getBoard()[i][j].isEmpty());
            }
        }
    }

    @Test
    public void removeWorkers_NotExistingWorkers_NotUpdateBoard(){
        m.removeWorkers(m.getPlayers().get(0));
        assertEquals(m.getPlayers().get(0).getWorker(0).getPosition(),null);
        assertEquals(m.getPlayers().get(0).getWorker(1).getPosition(),null);
        for(int i=0; i<m.getRows(); i++){
            for(int j=0;j<m.getColumns();j++){
                assertTrue(m.getBoard()[i][j].isEmpty());
            }
        }
    }
}
