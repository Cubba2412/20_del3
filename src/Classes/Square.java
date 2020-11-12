package Classes;

public class Square {

    private String name;
    private int price;
    private String color;

    public Square(String name, int price, String color) {
        this.name = name;
        this.price = price;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }
}
