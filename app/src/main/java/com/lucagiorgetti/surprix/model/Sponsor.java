package com.lucagiorgetti.surprix.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Utente on 06/03/2018.
 */

public class Sponsor {
    private String name;
    private String bannerImageUrl;
    private boolean clickable;
    private String url;
    private int order;

    public Sponsor(String name, String imageUrl, boolean clickable, String url, int order) {
        this.name = name;
        this.bannerImageUrl = imageUrl;
        this.clickable = clickable;
        this.url = url;
        this.order = order;
    }

    public Sponsor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public boolean isClickable() {
        return clickable;
    }

    public String getUrl() {
        return url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}