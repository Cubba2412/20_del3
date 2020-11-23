package Tests;

import Classes.Player;
import gui_fields.GUI_Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlayerTest {
    private Player player;
    //Do this before all the testing
    @Before
    public void init() {
        GUI_Player gui_player = new GUI_Player("Test name", 2000);
        player = new Player(gui_player, 18, 10);
    }

    //positiv test method
    @Test
    public void testName() {
        assertEquals("Test name", player.getName());
    }

    //NEgativ test method
    @Test
    public void testNameNegative() {
        assertNotEquals("Test not name", player.getName());
    }

    //positive test for age
    @Test
    public void testAge() {
        assertEquals(18, player.getAge());
    }

    //negative test for age
    @Test
    public void testAgeNegative() {
        assertNotEquals(10, player.getAge());
    }

    @Test
    public void testBankrupt() {
        GUI_Player gui_player = new GUI_Player("Test name", 0);
        player = new Player(gui_player, 18, 10);
        boolean expectedResult = true;
        assertEquals(expectedResult, player.isBankrupt());
    }
}