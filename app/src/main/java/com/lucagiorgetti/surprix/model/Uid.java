package com.lucagiorgetti.surprix.model;

import com.lucagiorgetti.surprix.utility.LoginFlowHelper;

public class Uid {
    String uid;
    String username;
    String provider;

    public Uid(String uid, String username, LoginFlowHelper.AuthMode authMode) {
        this.uid = uid;
        this.username = username;

        switch (authMode){
            case EMAIL_PASSWORD:
                this.provider = "password";
                break;
            case FACEBOOK:
                this.provider = "facebook.com";
                break;
        }
    }

    public Uid(String uid, User user) {
        this.uid = uid;
        this.username = user.getUsername();
        this.provider = user.isFacebook() ? "facebook.com" : "password";
    }

    public Uid(String uid, String username, boolean fromFacebook) {
        this.uid = uid;
        this.username = username;
        this.provider = fromFacebook ? "facebook.com" : "password";
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProvider() {
        return provider;
    }
}
