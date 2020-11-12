package Classes;

public class Player {

    private String name;
    private int age;
    private int balance;
    private int currentPositionIndex;

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

    public int getCurrentPositionIndex() {
        return currentPositionIndex;
    }

    public void setCurrentPositionIndex(int currentPositionIndex) {
        this.currentPositionIndex = currentPositionIndex;
    }

    public void increaseBalanceBy(int amount){
        this.balance += amount;
    }

    public void decreaseBalanceBy(int amount){
        this.balance -= amount;
    }
}
