package com.appdev.lgmm;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String userID;
    private String username;
    private String email;
    private String bio;
    private String profileImage;
    private Date dateCreated;

    public User() {

    }

    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        bio = "";
        profileImage = "";
        dateCreated = Calendar.getInstance().getTime();
    }

    public User(String userID, String username, String email, String bio, String profileImage) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.profileImage = profileImage;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateCreated = Calendar.getInstance().getTime();
    }

    public User(User user) {
        this(user.userID, user.username, user.email, user.bio, user.profileImage);
    }

    @Override
    public String toString() {
        return String.format("User\n" +
                "UserId: %s\n" +
                "Username: %s\n" +
                "Email: %s\n" +
                "Bio: %s\n", userID, username, email, bio);
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
