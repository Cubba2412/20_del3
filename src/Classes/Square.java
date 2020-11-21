package Classes;

import gui_fields.GUI_Field;

public class Square {

    private GUI_Field field;
    private int price;
    private SquareColor color;
    private SquareType squareType;

    public Square(GUI_Field field, int price, SquareColor color, SquareType squareType) {
        this.field = field;
        this.price = price;
        this.color = color;
        this.squareType = squareType;
    }

    public String getName() {
        return field.getDescription();
    }
    public String getSubText() {
        return field.getSubText();
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

    public GUI_Field getField() {
        return field;
    }
}
