package Classes;

import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;

public class Player {

    private GUI_Player guiplayer;
    private int age;
    private int currentSquareIndex;
    private boolean isInPrison;
    private boolean getOutOfJailCard;

    public Player(GUI_Player guiplayer, int age, int currentSquareIndex) {
        this.guiplayer = guiplayer;
        this.age = age;
        this.currentSquareIndex = currentSquareIndex;
        this.isInPrison = false;
        this.getOutOfJailCard = false;
    }
    public GUI_Player getGuiPlayer() {return this.guiplayer;}

    public String getName() {
        return this.guiplayer.getName();
    }

    public Color getCarColor() {
        return guiplayer.getPrimaryColor();
    }

    public int getAge() {
        return age;
    }

    public int getBalance() {
        return this.guiplayer.getBalance();
    }

    public int getCurrentSquareIndex() {
        return currentSquareIndex;
    }

    public void setCurrentSquareIndex(GUI gui, int currentPositionIndex) {
        //Remove player from old field
        gui.getFields()[this.currentSquareIndex].setCar(this.guiplayer, false);
        this.currentSquareIndex = currentPositionIndex;
        gui.getFields()[this.currentSquareIndex].setCar(this.guiplayer, true);
    }

    public void increaseBalanceBy(int amount) {
        int currentBalance = this.guiplayer.getBalance();
        this.guiplayer.setBalance(currentBalance + amount);
    }

    public void decreaseBalanceBy(int amount) throws NotEnoughBalanceException {
        int remainingBalance = this.guiplayer.getBalance() - amount;
        if (remainingBalance < 0) {
            throw new NotEnoughBalanceException();
        }

        this.guiplayer.setBalance(remainingBalance);
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
        return (this.guiplayer.getBalance() <= 0);
    }
}
