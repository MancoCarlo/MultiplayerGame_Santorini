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
        player=new Player("Luca", 21);
        God g = new God(0,"Carlo","a");
        ArrayList<God> gods = new ArrayList();
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
        player.putWorker(0, board, c);
        assertEquals(player.getWorker(0).getPosition(), c);
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

    @Test
    public void toString_printPlayer_returnString(){
        assertEquals(player.toString(), "Player{ nickname='Luca', age=21, card="+player.getCard().toString()+"}");
    }
}
