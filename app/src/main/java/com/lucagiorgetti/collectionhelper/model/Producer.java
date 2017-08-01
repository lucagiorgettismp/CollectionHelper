package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Producer implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "producer";
    public static final String COLUMN_PRODUCER_NAME = "name";
    public static final String COLUMN_PRODUCER_NATIONALITY = "nationality";

    private int id;
    private String name;
    private String nationality;

    public Producer(String name, String nationality){
        this.name = name;
        this.nationality = nationality;

    }

    public Producer(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Producer._ID));
        this.name = cursor.getString(cursor.getColumnIndex(Producer.COLUMN_PRODUCER_NAME));
        this.nationality = cursor.getString(cursor.getColumnIndex(Producer.COLUMN_PRODUCER_NATIONALITY));
    }

    public int getId(){ return id;}
    public String getName(){ return name;}
    public String getNationality(){ return nationality;}

    @Override
    public String toString(){
        return String.valueOf(name) + " - " + String.valueOf(nationality);
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCER_NAME, name);
        cv.put(COLUMN_PRODUCER_NATIONALITY, nationality);

        return cv;
    }
}