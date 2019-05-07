package com.example.myapplication.models;

public class Message {
    private String text;
    private boolean belongsToCurrentUser;
    private String sender;

    public Message(String sender, String text, boolean belongsToCurrentUser) {
        this.sender = sender;
        this.text = text;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }
    public String getSender() {
        return sender;
    }
    public String getUsername() {
        String username = sender.substring(0,4) + sender.substring(sender.length() - 4, sender.length());
        return username;
    }
    public void setBelongsToCurrentUser(boolean bool) {
        this.belongsToCurrentUser = bool;
    }

    public boolean belongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
