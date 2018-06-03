package com.voteandeat.voteandeat.Room.Model;

public class Room {

    public String id;
    public String name;
    public String password;
    public Boolean open;
    public Chat chat;
    public Member members;
    public Vote vote;

    public Room(){}

    public Room(String id,String name, String password, Boolean open, Chat chat) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.open = open;
        this.chat = chat;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Member getMembers() {
        return members;
    }

    public void setMembers(Member members) {
        this.members = members;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", open=" + open +
                ", chat=" + chat +
                ", members=" + members +
                ", vote=" + vote +
                '}';
    }
}
