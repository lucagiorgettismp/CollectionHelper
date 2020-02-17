package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class User {

    // private String name = null;
    // private String surname = null;
    private String email = null;
    private String username = null;
    // private String birthday = null;
    private String country = null;
    private int latitude = 0;
    private int longitude = 0;
    private boolean facebook = false;

    public User() {
    }

    public User(String email, String username, String country, Boolean facebook) {
        this.email = email;
        this.username = username;
        this.country = country;
        this.facebook = facebook;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public boolean isFacebook() {
        return facebook;
    }
}