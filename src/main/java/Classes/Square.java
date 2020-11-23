package Classes;

import gui_fields.GUI_Field;

public class Square {

    private GUI_Field field;
    private int price;
    private SquareColor color;
    private SquareType squareType;
    private int numHouses;
    private boolean hasHotel;

    public Square(GUI_Field field, int price, SquareColor color, SquareType squareType) {
        this.field = field;
        this.price = price;
        this.color = color;
        this.squareType = squareType;
        this.numHouses = 0;
        this.hasHotel = false;
    }

    public String getName() {
        return field.getDescription();
    }
    public String getSubText() {
        return field.getSubText();
    }

    public boolean hasHotel() {
        return hasHotel;
    }

    public int getNumHouses() {
        return numHouses;
    }

    public void setNumHouses(int numHouses) {
        this.numHouses = numHouses;
    }

    public void setHotel() {
        hasHotel = !hasHotel;
    }

    public int getPrice() {
        if(numHouses == 0) {
            return price/4;
        }
        else if (hasHotel) {
            return price*2;
        }
        else {
            return (price/4*numHouses);
        }
    }

    public int getFieldPrice() {
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
