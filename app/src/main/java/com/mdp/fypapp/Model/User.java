package com.mdp.fypapp.Model;

public class User {
    private String credit;
    private String id;
    private String imageURL;
    private String phone;
    private String rank;
    private String status;
    private String type;
    private String username;

    public User(){}

    public User(String credit, String id, String imageURL, String phone, String rank, String status, String type, String username) {
        this.credit = credit;
        this.id = id;
        this.imageURL = imageURL;
        this.phone = phone;
        this.rank = rank;
        this.status = status;
        this.type = type;
        this.username = username;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
