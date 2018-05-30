package com.voteandeat.voteandeat.Room.Model;

import java.util.ArrayList;

public class Chat {
    public ArrayList<Message> messages;

    public Chat() {
    }

    public Chat(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "messages=" + messages +
                '}';
    }
}
