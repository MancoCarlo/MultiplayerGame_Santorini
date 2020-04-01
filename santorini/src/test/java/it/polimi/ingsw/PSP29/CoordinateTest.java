package it.polimi.ingsw.PSP29;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoordinateTest {
    Coordinate c1 = null;
    Coordinate c2 = null;
    @Before public void SetUp(){
        c1= new Coordinate(1,2);
        Coordinate c2 = null;
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

}
