package Tests;
import Classes.Dice;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class DiceTest {
    Dice dice = new Dice();
    @Test
    public void Testroll() {
        int val = dice.roll();
        assertTrue(0 < val && val <= 6);
    }
    @Test
        public void testRandomness() {
            int[] vals = new int[60000];
            for (int i = 0;i<vals.length;i++) {
                vals[i] = dice.roll();
            }
            int[] freq = new int[5];
            for (int i = 1;i<freq.length+1;i++) {
                freq[i-1] = Collections.frequency(Arrays.asList(vals), i);
            }
            for (int i = 0;i<freq.length;i++) {
                assertTrue(freq[i] <= 9600 && freq[i] <= 10400);
            }
        }
}
