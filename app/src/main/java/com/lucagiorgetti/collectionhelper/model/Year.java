package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Year implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "year";
    public static final String COLUMN_YEAR_NUMBER = "year_number";
    public static final String COLUMN_YEAR_SEASON = "season";
    public static final String COLUMN_YEAR_ID = "year_id";

    private int id;
    private int year_number;
    private int yearId;
    private int season;

    public Year(int number, int season, int yearId){
        this.year_number=number;
        this.season=season;
        this.yearId=yearId;
    }

    public Year (Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Year._ID));
        this.year_number = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_YEAR_NUMBER));
        this.yearId = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_YEAR_ID));
        this.season = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_YEAR_SEASON));
    }


    public int getId(){ return id;}
    public int getYear(){ return year_number;}
    public int getSeason(){ return season;}
    public int getYearId() {return yearId;}

    @Override
    public String toString(){
        return String.valueOf(year_number) + " - " + String.valueOf(this.getId());
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_YEAR_NUMBER,year_number);
        cv.put(COLUMN_YEAR_SEASON, season);
        cv.put(COLUMN_YEAR_ID, yearId);
        return cv;
    }
}
