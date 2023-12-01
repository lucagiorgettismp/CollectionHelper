package com.lucagiorgetti.surprix.model;

import com.lucagiorgetti.surprix.utility.LoginFlowHelper;

public class Uid {
    String uid;
    String username;
    String provider;

    public Uid(String uid, String username, LoginFlowHelper.AuthMode authMode) {
        this.uid = uid;
        this.username = username;
        this.provider = authMode.getProvider();
    }

    public Uid() {

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
