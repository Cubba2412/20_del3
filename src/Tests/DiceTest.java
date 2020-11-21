package Tests;
import Classes.Dice;
import org.junit.Test;
import static org.junit.Assert.fail;

public class DiceTest {
    @Test
    public void testRoll() {
        int number1 = 0,number2 = 0,number3 = 0,number4 = 0,number5 = 0,number6 = 0;
        for (int i= 0;i<= 1000000;i++) {
            Dice dice = new Dice();
            int value = dice.roll();
            switch (value)
            {
                case 1:
                    number1++;
                    break;
                case 2:
                    number2++;
                    break;
                case 3:
                    number3++;
                    break;
                case 4:
                    number4++;
                    break;
                case 5:
                    number5++;
                    break;
                case 6:
                    number6++;
                    break;
                default:
                    fail("Unexpected value: " + value);
            }
        }
        System.out.println("Number1: "+number1+"\nNumber2: "+number2+"\nNumber3: "+number3+"\nNumber4: "+number4+
                "\nNumber5: "+number5+"\nNumber6: "+number6);
    }
}
