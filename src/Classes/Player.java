package Classes;

public class Player {

    private String name;
    private int age;
    private int balance;
    private int currentSquareIndex;
    private PlayerFigureType figureType;

    public Player(String name, int age, PlayerFigureType figureType, int balance) {
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.figureType = figureType;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getBalance() {
        return balance;
    }

    public int getCurrentSquareIndex() {
        return currentSquareIndex;
    }

    public void setCurrentSquareIndex(int currentPositionIndex) {
        this.currentSquareIndex = currentPositionIndex;
    }

    public void increaseBalanceBy(int amount){
        this.balance += amount;
    }

    public void decreaseBalanceBy(int amount){
        this.balance -= amount;
    }
}
