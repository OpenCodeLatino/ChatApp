package com.example.chatapp.Model;

public class ChatRoom {

    String title;
    String description;
    String uid;
    long lastMessageTime;

    public ChatRoom(String title, String description, long lastMessageTime) {
        this.title = title;
        this.description = description;
        this.lastMessageTime = lastMessageTime;
    }

    public ChatRoom() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
