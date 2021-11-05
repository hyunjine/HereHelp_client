package com.example.herehelp.activity_record;

public class Record_Item {

    private String nickname;
    private int category;
    private String price;
    private String time;

    public Record_Item(String nickname, int category, String price, String time) {
        this.nickname = nickname;
        this.category = category;
        this.price = price;
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public int getCategory() {
        return category;
    }

    public String getPrice() { return price; }

    public String getTime() {
        return time;
    }
}