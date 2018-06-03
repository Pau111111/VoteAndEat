package com.voteandeat.voteandeat.Room.Model;

public class Member {
    public String idUser;
    public String name;
    public String photoUrlUser;
    public String privileges;
    public Member(){

    }
    public Member(String idUser, String name,String photoUrlUser, String privileges) {
        this.idUser = idUser;
        this.name = name;
        this.photoUrlUser = photoUrlUser;
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

    public String getPhotoUrlUser() {
        return photoUrlUser;
    }

    public void setPhotoUrlUser(String photoUrlUser) {
        this.photoUrlUser = photoUrlUser;
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
                ", photoUrlUser='" + photoUrlUser + '\'' +
                ", privileges='" + privileges + '\'' +
                '}';
    }
}
