package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utente on 17/04/2017.
 */

public class User {

    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private Date birth_date;
    private Address address;
    private List<Surprise> missings = new ArrayList<Surprise>();
    private List<Surprise> doubles = new ArrayList<Surprise>();

    public User(String name, String surname, String email, String username, String password, Date birth_date, Address address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.birth_date = birth_date;
    }

    public User(String email, String username, String password) {
        this.name = null;
        this.surname = null;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = null;
        this.birth_date = null;
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

    public String getPassword() {
        return password;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public Address getAddress() {
        return address;
    }

    public List<Surprise> getMissings() {
        return missings;
    }

    public List<Surprise> getDoubles() {
        return doubles;
    }
}