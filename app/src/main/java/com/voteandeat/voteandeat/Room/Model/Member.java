package com.voteandeat.voteandeat.Room.Model;

public class Member {
    public String idUser;
    public String name;
    public String urlUser;
    public String privileges;
    public Member(){

    }
    public Member(String idUser, String name,String urlUser, String privileges) {
        this.idUser = idUser;
        this.name = name;
        this.urlUser = urlUser;
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

    public String getUrlUser() {
        return urlUser;
    }

    public void setUrlUser(String urlUser) {
        this.urlUser = urlUser;
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
                ", urlUser='" + urlUser + '\'' +
                ", privileges='" + privileges + '\'' +
                '}';
    }
}
