package it.polimi.ingsw.PSP29.model;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoordinateTest {
    Coordinate c1 = null;
    Coordinate c2 = null;
    Match m =null;
    @Before public void SetUp(){
        c1= new Coordinate(1,2);
        Coordinate c2 = null;
        m = new Match();
    }
    @After
    public void TearDown(){
        c2=null;
    }
    @Test
    public void TestgetX_and_getY() {
        assertEquals(c1.getX(), 1);
        assertEquals(c1.getY(), 2);
    }
    @Test
    public void isNear_correctInput_trueOutput(){
        c2= new Coordinate(2,3);
        assertTrue(c1.isNear(c2));
    }
    @Test
    public void isNear_correctInput_falseOutput(){
        c2= new Coordinate(1,4);
        assertFalse(c1.isNear(c2));
    }
    @Test
    public void isNear_sameInput_falseOutput(){
        c2= new Coordinate(1,2);
        assertFalse(c1.isNear(c2));
    }
    @Test
    public void equals_nullInput_falseOutput(){
        assertFalse(c1.equals(null));
    }

    @Test
    public void equals_wrongInput_falseOutput(){
        int x= 1;
        assertFalse(c1.equals(x));
    }
    @Test
    public void nextCoordinate_correctInput1_correctOutput() {
        c2 = new Coordinate(2, 1);
        c1 = new Coordinate(1, 0);
        Coordinate c3 = new Coordinate(3, 2);
        assertEquals(c1.nextCoordinate(m, c2), c3);
    }
    @Test
    public void nextCoordinate_correctInput2_correctOutput() {
        c2= new Coordinate(1,1);
        c1= new Coordinate(2,2);
        Coordinate c3 = new Coordinate(0, 0);
        assertEquals(c1.nextCoordinate(m, c2), c3);
    }
    @Test
    public void nextCoordinate_sameInput_sameOutput() {
        c2= new Coordinate(1,0);
        c1= new Coordinate(1,0);
        assertEquals(c1.nextCoordinate(m, c2), c1);
    }
    @Test
    public void nextCoordinate_correctInput_notSignificantOutput() {
        c2 = new Coordinate(4, 4);
        c1 = new Coordinate(4, 3);
        assertEquals(c1.nextCoordinate(m, c2), c2);
    }
    @Test
    public void nextCoordinate_wrongInput_correctOutput() {
        c2= new Coordinate(2,1);
        c1= new Coordinate(1,4);
        assertEquals(c1.nextCoordinate(m, c2), c2);
    }
    @Test
    public void Test_toString() {
        Coordinate c = new Coordinate (2,4);
        String s = c.toString();
        assertEquals("2,4", s);
    }

}
