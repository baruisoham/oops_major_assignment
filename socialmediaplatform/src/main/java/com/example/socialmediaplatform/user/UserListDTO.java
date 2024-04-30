package com.example.socialmediaplatform.user;

public class UserListDTO {
    private String name;
    private Long userID;
    private String email;

    // Constructors, getters, and setters

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}