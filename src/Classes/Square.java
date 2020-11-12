package Classes;

public class Square {

    private String name;
    private int price;
    private SquareColor color;
    private SquareType squareType;

    public Square(String name, int price, SquareColor color, SquareType squareType) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.squareType = squareType;
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

    public SquareType getSquareType() {
        return squareType;
    }
}
