package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Set {
    private String name;
    private int year;
    private Producer producer;
    private String season;
    private String nation;
    private String img_path;

    public Set(String name, int year, Producer producer, String season, String nation, String img_path) {
        this.name = name;
        this.year = year;
        this.producer = producer;
        this.season = season;
        this.nation = nation;
        this.img_path = img_path;
    }

    public Set() {
          name = null;
          year = 0;
          producer = null;
          season = null;
          nation = null;
          img_path = null;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public Producer getProducer() {
        return producer;
    }

    public String getSeason() {
        return season;
    }

    public String getNation() {
        return nation;
    }

    public String getImg_path() {
        return img_path;
    }
}