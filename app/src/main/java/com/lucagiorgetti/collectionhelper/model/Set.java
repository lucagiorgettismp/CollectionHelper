package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Set {
    private String id = null;
    private String name = null;
    private int year = -1;
    private String product = null;
    private String producer = null;
    private String nation = null;
    private String img_path = null;
    private String color = null;
    private String category = null;

    public Set(String name, int year, Product product, String nation, String img_path, String color, String category) {
        this.name = name;
        this.year = year;
        this.product = product.getName();
        this.producer = product.getProducer().getName();
        this.nation = nation;
        this.img_path = img_path;
        this.color = color;
        this.category = category;
        this.id = name.replaceAll("\\s+","") + "_" + year;
    }

    public Set() {
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getProduct() {
        return product;
    }

    public String getProducer() {
        return producer;
    }

    public String getNation() {
        return nation;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}