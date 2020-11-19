package Tests;

import Classes.Player;
import Classes.PlayerFigureType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void init() {
        player = new Player("Test name", 18, PlayerFigureType.Hunden, 10);
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
        boolean expectedResult = true;
        player.increaseBalanceBy(-20);
        assertEquals(expectedResult, player.isBankrupt());
    }
}
