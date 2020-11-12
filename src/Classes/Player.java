package Classes;

public class Player {

    private String navn;
    private int age;

    public Player(String navn, int age) {
        this.navn = navn;
        this.age = age;
    }

    public String getNavn() {
        return navn;
    }

    public int getAge() {
        return age;
    }
}
