package com.voteandeat.voteandeat.Room.Model;

public class VotePlace {
    public String idUser;
    public String address;
    public String name;
    public String photoUrl;
    public Double latitude;
    public Double longitude;
    public String stars;
    public Boolean openNow;
    public String mapUrl;
    public String idVotePlace;

    public VotePlace(){

    }
    public VotePlace(String idUser, String address, String name, String photoUrl, Double latitude, Double longitude, String stars, String mapUrl, Boolean openNow, String idVotePlace) {
        this.idUser = idUser;
        this.address = address;
        this.name = name;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stars = stars;
        this.mapUrl = mapUrl;
        this.openNow = openNow;
        this.idVotePlace = idVotePlace;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public String getIdVotePlace() {
        return idVotePlace;
    }

    public void setIdVotePlace(String idVotePlace) {
        this.idVotePlace = idVotePlace;
    }

    @Override
    public String toString() {
        return "VotePlace{" +
                "idUser='" + idUser + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", stars='" + stars + '\'' +
                ", openNow=" + openNow +
                ", mapUrl='" + mapUrl + '\'' +
                ", idVotePlace='" + idVotePlace + '\'' +
                '}';
    }
}
