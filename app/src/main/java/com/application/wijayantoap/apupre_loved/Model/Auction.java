package com.application.wijayantoap.apupre_loved.Model;

public class Auction {
    private String Owner, LastMessage, Time;

    public Auction() {
    }

    public Auction(String owner, String lastMessage, String time) {
        Owner = owner;
        LastMessage = lastMessage;
        Time = time;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
