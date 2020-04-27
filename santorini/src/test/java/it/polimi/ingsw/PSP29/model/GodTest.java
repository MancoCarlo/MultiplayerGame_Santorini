package it.polimi.ingsw.PSP29.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GodTest {
    God g = null;
    @Before
    public void setUp() throws Exception {
        g = new God(1,"Carlo", "Il più forte di tutti gli dei");
        assertEquals("Carlo", g.getName());
        assertEquals(1,g.getID());
        assertEquals("Il più forte di tutti gli dei", g.getDescription());
    }

    @Test
    public void toStringTest_printGod_returnString() {
        assertEquals(g.toString(),"{ID=1, name='Carlo\', description='Il più forte di tutti gli dei\'}");
    }
}
