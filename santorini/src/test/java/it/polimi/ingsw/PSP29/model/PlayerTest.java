package it.polimi.ingsw.PSP29.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {
    Player player=null;

    @Before
    public void setUp(){
        player=new Player(1, "Luca", 21);
    }

    @After
    public void tearDown(){}

    @Test
    public void newPlayer_correctInput(){
        assertNotNull(player.workers.get(0));
        assertEquals(player.workers.get(0).getID(), 0);
        assertEquals(player.workers.get(0).getIDplayer(), "Luca");
        assertNull(player.workers.get(0).getPosition());
        assertNotNull(player.workers.get(1));
        assertEquals(player.workers.get(1).getID(), 1);
        assertEquals(player.workers.get(1).getIDplayer(), "Luca");
        assertNull(player.workers.get(1).getPosition());
    }

    @Test
    public void putWorker_correctInput_emptyBox(){
        Coordinate c=new Coordinate(1, 4);
        Box[][] board = new Box[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Box(i, j);
            }
        }
        boolean b = player.putWorker(0, board, c);
        assertEquals(player.getWorker(0).getPosition(), c);
        assertTrue(b);
    }

    @Test
    public void putWorker_correctInput_notEmptyBox(){
        Coordinate c=new Coordinate(1, 4);
        Coordinate c1= new Coordinate(1, 3);
        Box[][] board = new Box[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Box(i, j);
            }
        }
        player.workers.get(0).setPosition(c1);
        board[c.getX()][c.getY()].changeState();
        boolean b = player.putWorker(0, board, c);
        assertNotEquals(player.getWorker(0).getPosition(), c);
        assertFalse(b);
    }

    @Test
    public void putWorker_workerAlreadyPlaced_EmptyBox(){
        Coordinate c=new Coordinate(1, 4);
        Coordinate c1= new Coordinate(1, 3);
        Box[][] board = new Box[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Box(i, j);
            }
        }
        player.workers.get(0).setPosition(c1);
        boolean b = player.putWorker(0, board, c);
        assertEquals(player.getWorker(0).getPosition(), c);
        assertTrue(b);
    }

    @Test
    public void getID_correctOutput(){
        assertEquals(player.getID(), 1);
    }

    @Test
    public void getNickname_correctOutput(){
        assertEquals(player.getNickname(), "Luca");
    }

    @Test
    public void getAge_correctOutput(){
        assertEquals(player.getAge(), 21);
    }

    @Test
    public void getWorker_correctOutput(){
        assertEquals(player.getWorker(0), player.workers.get(0));
        assertEquals(player.getWorker(1), player.workers.get(1));
    }
}
