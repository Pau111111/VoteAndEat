package com.voteandeat.voteandeat.Room.Model;

import java.util.ArrayList;

public class Vote {

    public boolean state;
    public ArrayList<VotePlace> votePlaces;

    public Vote(boolean state, ArrayList<VotePlace> votePlaces) {
        this.state = state;
        this.votePlaces = votePlaces;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public ArrayList<VotePlace> getVotePlaces() {
        return votePlaces;
    }

    public void setVotePlaces(ArrayList<VotePlace> votePlaces) {
        this.votePlaces = votePlaces;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "state=" + state +
                ", votePlaces=" + votePlaces +
                '}';
    }
}
