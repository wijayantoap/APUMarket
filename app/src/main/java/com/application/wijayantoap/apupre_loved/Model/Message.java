package com.application.wijayantoap.apupre_loved.Model;

public class Message {

    private String Sender, Message, TimeStamp;

    public Message() {
    }

    public Message(String sender, String message, String timeStamp) {
        Sender = sender;
        Message = message;
        TimeStamp = timeStamp;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }
}
