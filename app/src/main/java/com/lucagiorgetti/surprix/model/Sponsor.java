package com.lucagiorgetti.surprix.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Utente on 06/03/2018.
 */

public class Sponsor {
    private String name;
    private Drawable banner;
    private boolean clickable;
    private String url;

    public Sponsor(String name, Drawable banner, boolean clickable, String url) {
        this.name = name;
        this.banner = banner;
        this.clickable = clickable;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getBanner() {
        return banner;
    }

    public boolean isClickable() {
        return clickable;
    }

    public String getUrl() {
        return url;
    }
}