package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Set implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "collection_set";
    public static final String COLUMN_SET_NAME = "name";
    public static final String COLUMN_PRODUCER_ID = "producer_id";
    public static final String COLUMN_YEAR_ID = "year_id";
    public static final String COLUMN_SET_IMAGE_PATH = "img_path";

    private int id;
    private String name;
    private int year_id;
    private int producer_id;
    private String img_path;


    public Set(String name, int year_id, String img_path, int producer_id){
        this.name = name;
        this.year_id = year_id;
        this.img_path = img_path;
        this.producer_id = producer_id;

    }

    public Set (Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Set._ID));
        this.name = cursor.getString(cursor.getColumnIndex(Set.COLUMN_SET_NAME));
        this.year_id = cursor.getInt(cursor.getColumnIndex(Set.COLUMN_YEAR_ID));
        this.img_path = cursor.getString(cursor.getColumnIndex(Set.COLUMN_SET_IMAGE_PATH));
        this.producer_id = cursor.getInt(cursor.getColumnIndex((COLUMN_PRODUCER_ID)));
    }

    public int getId(){ return id;}
    public String getName(){ return name;}
    public int getYearId() { return year_id;}
    public String getImagePath() { return img_path;}
    public int getProducerId(){ return producer_id;}

    @Override
    public String toString(){
        return String.valueOf(name) + " " +id;
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SET_NAME, name);
        cv.put(COLUMN_YEAR_ID, year_id);
        cv.put(COLUMN_SET_IMAGE_PATH, img_path);
        cv.put(COLUMN_PRODUCER_ID, producer_id);

        return cv;
    }
}