package com.example.model;

public class Item {


    public int position;

    public String itemText;

    public Item(int position,String itemText) {
        this.itemText = itemText;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getItemText() {
        return itemText;
    }

    @Override
    public String toString() {
        return "Item{" +
                "position=" + position +
                ", itemText='" + itemText + '\'' +
                '}';
    }
}
