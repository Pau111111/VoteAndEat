package com.voteandeat.voteandeat.Room.Model;

public class Message {
    public String usedId;
    public String userName;
    public String message;

    public Message() {
    }

    public Message(String usedId, String userName, String message) {
        this.usedId = usedId;
        this.userName = userName;
        this.message = message;
    }

    public String getUsedId() {
        return usedId;
    }

    public void setUsedId(String usedId) {
        this.usedId = usedId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "usedId='" + usedId + '\'' +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
