package com.lucagiorgetti.surprix.model;

import com.lucagiorgetti.surprix.utility.LoginFlowHelper;

/**
 * Created by Utente on 17/04/2017.
 */

public class User {
    private String email = null;
    private String username = null;
    private String country = null;
    private String provider = null;

    public User(String email, String username, String country, LoginFlowHelper.AuthMode authMode) {
        this.email = email;
        this.username = username;
        this.country = country;
        this.provider = authMode.getProvider();
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String clearedEmail() {
        return email.replaceAll(",", "\\.");
    }

    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }

    public String getProvider() {
        return provider;
    }
}