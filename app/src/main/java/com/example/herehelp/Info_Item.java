package com.example.herehelp;

public class Info_Item {

    private String id;
    private int category;
    private String price;
    private String time;

    public Info_Item(String id, int category, String price, String time) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.time = time;
    }
    public String getID() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public String getPrice() { return price; }

    public String getTime() {
        return time;
    }
}