package Classes;

public class Player {

    private String navn;
    private int age;
    private int balance;
    private int currentPositionIndex;

    public Player(String navn, int age, int balance) {
        this.navn = navn;
        this.age = age;
        this.balance = balance;
    }

    public String getNavn() {
        return navn;
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
