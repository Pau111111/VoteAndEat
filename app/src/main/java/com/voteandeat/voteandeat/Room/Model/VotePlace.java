package com.voteandeat.voteandeat.Room.Model;

public class VotePlace {
    public String idUser;
    public String address;
    public String name;
    public String photoUrl;
    public String latitude;
    public String longitude;
    public Double stars;
    public String mapUrl;

    public VotePlace(String idUser, String address, String name, String photoUrl, String latitude, String longitude, Double stars, String mapUrl) {
        this.idUser = idUser;
        this.address = address;
        this.name = name;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stars = stars;
        this.mapUrl = mapUrl;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    @Override
    public String toString() {
        return "VotePlace{" +
                "idUser='" + idUser + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", stars=" + stars +
                ", mapUrl='" + mapUrl + '\'' +
                '}';
    }
}
