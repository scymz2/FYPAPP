package com.mdp.fypapp.Model;

public class Chat {

    private int isBot;
    private String message;
    private boolean isReceived;

    public Chat(){}

    public Chat(int isBot, String message, Boolean isReceived){
        this.isBot = isBot;
        this.message = message;
        this.isReceived = isReceived;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public int getIsBot() {
        return isBot;
    }

    public void setIsBot(int isBot) {
        this.isBot = isBot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
