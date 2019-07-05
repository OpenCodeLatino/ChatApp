package com.example.chatapp.Model;


public class Message {

    String senderName;
    String senderUid;
    String avatar;
    String image;
    String messageType;
    String message;
    long timestamp;


    public Message(String senderName, String senderUid, String avatar, String image, String messageType, String message, long timestamp) {
        this.senderName = senderName;
        this.senderUid = senderUid;
        this.avatar = avatar;
        this.image = image;
        this.messageType = messageType;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
