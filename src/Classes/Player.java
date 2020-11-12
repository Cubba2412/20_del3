package Classes;

public class Player {

    private String name;
    private int age;
    private int balance;
    private int currentSquareIndex;

    public Player(String name, int age, int balance) {
        this.name = name;
        this.age = age;
        this.balance = balance;
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
