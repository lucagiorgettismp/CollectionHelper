package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class User {
    private String email = null;
    private String username = null;
    private String country = null;

    public User() {
    }

    public User(String email, String username, String country) {
        this.email = email;
        this.username = username;
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public String getCleanedEmail() {
        return email.replaceAll(",", "\\.");
    }

    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }
}