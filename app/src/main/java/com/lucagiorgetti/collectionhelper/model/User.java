package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class User implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_SURNAME = "surname";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_BIRTH_DATE = "birth_date";
    public static final String COLUMN_USER_NATIONALITY = "nationality";

    private int id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private String birth_date;
    private String nationality;

    public User(String name, String surname, String email, String username, String password, String birth_date, String nationality) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.birth_date = birth_date;
        this.nationality = nationality;
    }

    public User(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(User._ID));
        this.name = cursor.getString(cursor.getColumnIndex(User.COLUMN_USER_NAME));
        this.surname = cursor.getString(cursor.getColumnIndex(User.COLUMN_USER_SURNAME));
        this.email = cursor.getString(cursor.getColumnIndex(User.COLUMN_USER_EMAIL));
        this.username = cursor.getString(cursor.getColumnIndex(User.COLUMN_USER_USERNAME));
        this.password = cursor.getString(cursor.getColumnIndex(User.COLUMN_USER_PASSWORD));
        this.birth_date = cursor.getString(cursor.getColumnIndex(User.COLUMN_USER_NAME));
    }

    public int getId() { return id;}
    public String getName() { return name;}
    public String getSurname() { return surname;}
    public String getEmail() { return email;}
    public String getUsername() { return username;}
    public String getPassword() { return password;}
    public String getBirth_date() { return birth_date;}
    public String getNationality() { return nationality;}

    @Override
    public String toString(){
        return String.valueOf(username);
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        cv.put(COLUMN_USER_SURNAME, surname);
        cv.put(COLUMN_USER_EMAIL, email);
        cv.put(COLUMN_USER_USERNAME, username);
        cv.put(COLUMN_USER_PASSWORD, password);
        cv.put(COLUMN_USER_BIRTH_DATE, birth_date);
        cv.put(COLUMN_USER_NATIONALITY, nationality);

        return cv;
    }
}