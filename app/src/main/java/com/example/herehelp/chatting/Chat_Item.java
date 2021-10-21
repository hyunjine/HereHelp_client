package com.example.herehelp.chatting;

public class Chat_Item {

    private String nickname;
    private String msg;
    private String time;
    private int viewType;
    private String opponent_nickname;

    public Chat_Item( String nickname, String msg, String time, int viewType, String opponent_nickname) {
        this.nickname = nickname;
        this.msg = msg;
        this.viewType = viewType;
        this.time = time;
        this.opponent_nickname = opponent_nickname;
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

    public String getOpponent_nickname() { return opponent_nickname; }
}