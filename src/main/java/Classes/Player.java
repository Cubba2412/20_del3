package Classes;

import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;

public class Player {

    private GUI_Player player;
    private int age;
    private int currentSquareIndex;
    private boolean isInPrison;
    private boolean getOutOfJailCard;

    public Player(GUI_Player player, int age, int currentSquareIndex) {
        this.player = player;
        this.age = age;
        this.currentSquareIndex = currentSquareIndex;
        this.isInPrison = false;
        this.getOutOfJailCard = false;
    }
    public GUI_Player getGuiPlayer() {return this.player;}

    public String getName() {
        return this.player.getName();
    }

    public Color getCarColor() {
        return player.getPrimaryColor();
    }

    public int getAge() {
        return age;
    }

    public int getBalance() {
        return this.player.getBalance();
    }

    public int getCurrentSquareIndex() {
        return currentSquareIndex;
    }

    public void setCurrentSquareIndex(GUI gui, int currentPositionIndex) {
        //Remove player from old field
        gui.getFields()[this.currentSquareIndex].setCar(this.player, false);
        this.currentSquareIndex = currentPositionIndex;
        gui.getFields()[this.currentSquareIndex].setCar(this.player, true);
    }

    public void increaseBalanceBy(int amount) {
        int currentBalance = this.player.getBalance();
        this.player.setBalance(currentBalance + amount);
    }

    public void decreaseBalanceBy(int amount) throws NotEnoughBalanceException {
        int remainingBalance = this.player.getBalance() - amount;
        if (remainingBalance < 0) {
            throw new NotEnoughBalanceException();
        }

        this.player.setBalance(remainingBalance);
    }

    public boolean hasJailFreeCard() {return getOutOfJailCard;}

    public void setGetOutOfJailCard() {
        getOutOfJailCard = !getOutOfJailCard;
    }

    public boolean isInPrison() {
        return isInPrison;
    }

    public void setInPrison(boolean inPrison) {
        isInPrison = inPrison;
    }

    public boolean isBankrupt() {
        return (this.player.getBalance() <= 0);
    }
}
