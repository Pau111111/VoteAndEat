package com.voteandeat.voteandeat.Room.Model;

import android.text.Editable;

public class Room {

    public String name;
    public Boolean open;
    public Chat chat;
    public Member members;
    public Vote vote;

    public Room(String name, Boolean open, Chat chat, Member members) {
        this.name = name;
        this.open = open;
        this.chat = chat;
        this.members = members;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", open=" + open +
                ", chat=" + chat +
                ", members=" + members +
                ", vote=" + vote +
                '}';
    }
}
