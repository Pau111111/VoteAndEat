package com.voteandeat.voteandeat.Room.Model;

public class Member {
    public String idUser;
    public String name;
    public String privileges;
    public Member(){

    }
    public Member(String idUser, String name, String privileges) {
        this.idUser = idUser;
        this.name = name;
        this.privileges = privileges;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "Member{" +
                "idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", privileges='" + privileges + '\'' +
                '}';
    }
}
