package com.voteandeat.voteandeat.Room.Model;

public class UserVote {
    public String idUser;
    public String idVotePlace;

    public UserVote() {}

    public UserVote(String idUser, String idVotePlace) {
        this.idUser = idUser;
        this.idVotePlace = idVotePlace;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdVotePlace() {
        return idVotePlace;
    }

    public void setIdVotePlace(String idVotePlace) {
        this.idVotePlace = idVotePlace;
    }

    @Override
    public String toString() {
        return "UserVote{" +
                "idUser='" + idUser + '\'' +
                ", idVotePlace='" + idVotePlace + '\'' +
                '}';
    }
}