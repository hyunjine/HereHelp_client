package com.example.herehelp.chatting;

public class Chat_Item {

    private String nickname;
    private String msg;
    private String time;
    private int viewType;

    public Chat_Item( String nickname, String msg, String time, int viewType) {
        this.nickname = nickname;
        this.msg = msg;
        this.viewType = viewType;
        this.time = time;
    }
    public String getNickname() {
        return nickname;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() { return time; }

    public int getViewType() {
        return viewType;
    }
}