package Classes;

import java.util.Random;

public class Dice {

    private Random random = new Random();

    private int generateRandomDiceValue(){
        int diceMinimumValue = 1;
        int diceMaximumValue = 6;
        return diceMinimumValue + random.nextInt((diceMaximumValue - diceMinimumValue) + 1);
    }

    public int roll()
    {
        return generateRandomDiceValue();
    }
}
