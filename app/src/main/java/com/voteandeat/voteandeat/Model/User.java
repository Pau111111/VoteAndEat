package com.voteandeat.voteandeat.Model;

public class User {
    public String name;
    public String email;
    public String photourl;

    public User(){

    }
    public User(String name) {
        this.name = name;
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
