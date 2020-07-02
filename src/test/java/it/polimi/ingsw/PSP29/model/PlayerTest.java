package it.polimi.ingsw.PSP29.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player=null;

    @Before
    public void setUp(){
        player=new Player("Luca", 21, 1);
        God g = new God(0,"Carlo","a");
        ArrayList<God> gods = new ArrayList<God>();
        gods.add(g);
        player.setCard(gods, 0);
        assertEquals(player.getCard(), g);
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
        assertTrue(board[c.getX()][c.getY()].isEmpty());
        player.putWorker(0, board, c);
        assertEquals(player.getWorker(0).getPosition(), c);
        assertFalse(board[c.getX()][c.getY()].isEmpty());
        assertTrue(c.equals(player.getWorker(0).getPrev_position()));
    }

    @Test
    public void putWorker_withPrevPosition_correctOutput(){
        Coordinate c = new Coordinate(1, 4);
        Coordinate cnext = new Coordinate(1, 3);
        Box[][] board = new Box[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Box(i, j);
            }
        }
        player.putWorker(0, board, c);
        assertFalse(board[c.getX()][c.getY()].isEmpty());
        player.putWorker(0, board, cnext);
        assertEquals(player.getWorker(0).getPosition(), cnext);
        assertTrue(c.equals(player.getWorker(0).getPrev_position()));
        assertTrue(board[c.getX()][c.getY()].isEmpty());
        assertFalse(board[cnext.getX()][cnext.getY()].isEmpty());

    }

    @Test
    public void getId_correctOutput(){
        assertEquals(player.getId(), 1);
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
    public void getInGame_setInGame_CorrectOutput(){
        assertFalse(player.getInGame());
        player.setInGame(true);
        assertTrue(player.getInGame());
    }

    @Test
    public void getWorker_correctOutput(){
        assertEquals(player.getWorker(0), player.workers.get(0));
        assertEquals(player.getWorker(1), player.workers.get(1));
    }

    @Test public void printWorkers_correctOutput(){
        assertEquals(2, player.workers.size());
        String s = "Workers:\n" + "1) " + player.workers.get(0).toString() + "2) " + player.workers.get(1).toString();
        assertEquals(s, player.printWorkers());
    }

    @Test
    public void toString_printPlayer_returnString(){
        assertEquals(player.toString(), "Player{ nickname='Luca', age=21, card="+player.getCard().toString()+"}");
    }
}
