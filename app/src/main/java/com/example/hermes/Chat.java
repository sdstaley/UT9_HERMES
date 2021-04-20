package com.example.hermes;

public class Chat {
    private String sender;
    private String message;
    private String time;

    public Chat(String sender, String message, String time){
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    public Chat(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
