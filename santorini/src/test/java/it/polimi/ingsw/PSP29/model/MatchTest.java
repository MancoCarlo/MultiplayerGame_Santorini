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
        m.getPlayers().add(new Player("Luca", 23));
        m.getPlayers().add(new Player("Letizia", 22));
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

    @Test
    public void getPlayer_NotFoundPlayer_ReturnNull(){
        Player p1 = m.getPlayer("Carlo");
        assertEquals(p1, null);
    }

    @Test
    public void addPlayer_CorrectAddPlayer_Return(){
        Player p1 = new Player("Werner", 24);
        m.addPlayer(p1);
        assertEquals(p1, m.getPlayer("Werner"));
    }

    @Test
    public void resetBoard_CorrectReset_Return(){
        Box[][] b = m.getBoard();
        b[1][3].setLevelledUp(true);
        m.resetBoard();
        for(int i = 0; i<m.getRows();i++){
            for(int j=0;j<m.getColumns();j++){
                assertEquals(b[i][j].getlevelledUp(), false);
            }
        }
    }

    @Test
    public void removePlayer_CorrectRemove_Return(){
        Player p = m.getPlayer("Luca");
        m.removePlayer(p);
        assertTrue(m.getPlayers().size()==1);
    }

    @Test
    public void sortPlayers_playersSortFor_Return(){
        m.sortPlayers();
        Player p1 = m.getPlayer("Luca");
        Player p2 = m.getPlayer("Letizia");
        assertTrue(m.getPlayers().get(0)==p2);
        assertTrue(m.getPlayers().get(1)==p1);
    }

    @Test
    public void loadGods_vectorSizeCorrect_Return(){
        m.loadGods();
        assertTrue(m.getGods().size()==9);
    }

    @Test
    public void alreadyIn_PlayerInArrayList_ReturnTrue(){
        assertTrue(m.alreadyIn("Luca"));
    }

    @Test
    public void alreadyIn_PlayerNotInArrayList_ReturnFalse(){
        assertFalse(m.alreadyIn("werner"));
    }

    @Test
    public void printMethods_OutputPrint_ReturnString(){
        assertEquals(m.printBoard(),"Gameboard\n  \t" +
                "0 \t1 \t2 \t3 \t4 \t\n" +
                "0 \t00\t00\t00\t00\t00\t\n" +
                "1 \t00\t00\t00\t00\t00\t\n" +
                "2 \t00\t00\t00\t00\t00\t\n" +
                "3 \t00\t00\t00\t00\t00\t\n" +
                "4 \t00\t00\t00\t00\t00\t\n");
        assertEquals(m.printGodlist(),"Gods: \n");
        assertEquals(m.printPlayers(),"Players: \n"+ "- Luca, 23 years old\n" + "- Letizia, 22 years old\n");
    }
}
