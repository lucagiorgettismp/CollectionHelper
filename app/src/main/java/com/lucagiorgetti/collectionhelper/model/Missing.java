package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Missing implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "missing";
    public static final String COLUMN_USER_ID= "user_id";
    public static final String COLUMN_SURPRISE_ID = "surprise_code";
    public static final String COLUMN_MISSING_ID = "missing_id";

    private int id;
    private int user_id;
    private String surprise_code;
    private int missingId;

    public Missing(int user_id, String surprise_code, int missingId) {
        this.user_id = user_id;
        this.surprise_code = surprise_code;
        this.missingId = missingId;
    }

    public Missing(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Missing._ID));
        this.user_id = cursor.getInt(cursor.getColumnIndex(Missing.COLUMN_USER_ID));
        this.surprise_code = cursor.getString(cursor.getColumnIndex(Missing.COLUMN_SURPRISE_ID));
        this.missingId = cursor.getInt(cursor.getColumnIndex(Missing.COLUMN_MISSING_ID));
    }

    public int getId(){ return id;}
    public int getUserId(){ return user_id;}
    public String getSurpriseId() {return surprise_code;}
    public int getMissingId(){ return missingId;}


    @Override
    public String toString(){
        return String.valueOf(user_id) + " - " + String.valueOf(surprise_code);
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, user_id);
        cv.put(COLUMN_SURPRISE_ID, surprise_code);
        cv.put(COLUMN_MISSING_ID, missingId);

        return cv;
    }
}