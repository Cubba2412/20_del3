package Classes;

public class Square {

    private String name;
    private int price;
    private SquareColor color;

    public Square(String name, int price, SquareColor color) {
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

    public SquareColor getColor() {
        return color;
    }
}
