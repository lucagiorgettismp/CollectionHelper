package com.lucagiorgetti.surprix.model;

import android.graphics.drawable.Drawable;
import android.media.Image;

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

    public void setBanner(Drawable banner) {
        this.banner = banner;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}