package com.appdev.lgmm;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String userID;
    private String username;
    private String email;
    private String profileImage;
    private Date dateCreated;

    public User() {

    }

    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        profileImage = null;
        dateCreated = Calendar.getInstance().getTime();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
