package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Utente on 17/04/2017.
 */

public class User {

    private String name = null;
    private String surname = null;
    private String email = null;
    private String username = null;
    private String birthday = null;
    private String country = null;
    private int latitude = 0;
    private int longitude = 0;

    public User() {
    }

    public User(String name, String surname, String email, String username, String date, String country) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.birthday = date;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthday() throws ParseException {
        return birthday;
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
}