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
    public static final String COLUMN_PRODUCER_ID = "producer_id";
    public static final String COLUMN_PRODUCER_NAME = "name";
    public static final String COLUMN_PRODUCER_NATION = "nation";

    private int id;
    private String name;
    private String nation;
    private int producerId;

    public Producer(String name, String nation, int producerId){
        this.name = name;
        this.nation = nation;
        this.producerId = producerId;
    }

    public Producer(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Producer._ID));
        this.name = cursor.getString(cursor.getColumnIndex(Producer.COLUMN_PRODUCER_NAME));
        this.nation = cursor.getString(cursor.getColumnIndex(Producer.COLUMN_PRODUCER_NATION));
        this.producerId = cursor.getInt(cursor.getColumnIndex(Producer.COLUMN_PRODUCER_ID));
    }

    public int getId(){ return id;}
    public String getName(){ return name;}
    public String getNation(){ return nation;}
    public int getProducerId(){ return producerId;}

    @Override
    public String toString(){
        return String.valueOf(name) + " - " + String.valueOf(nation);
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCER_NAME, name);
        cv.put(COLUMN_PRODUCER_NATION, nation);
        cv.put(COLUMN_PRODUCER_ID, producerId);

        return cv;
    }
}