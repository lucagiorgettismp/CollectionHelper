package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Surprise implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "surprise";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_SURPRISE_IMG_PATH = "surprise_image_path";
    public static final String COLUMN_SET_ID = "set_id";

    private int id;
    private String desc;
    private String img_path;
    private String code;
    private int set_id;

    public Surprise(String code, String desc, String img_path, int set_id) {
        this.desc = desc;
        this.code = code;
        this.img_path = img_path;
        this.set_id = set_id;
    }

    public Surprise (Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Surprise._ID));
        this.code = cursor.getString(cursor.getColumnIndex(Surprise.COLUMN_CODE));
        this.desc = cursor.getString(cursor.getColumnIndex(Surprise.COLUMN_DESCRIPTION));
        this.img_path = cursor.getString(cursor.getColumnIndex(Surprise.COLUMN_SURPRISE_IMG_PATH));
        this.set_id = cursor.getInt(cursor.getColumnIndex(Surprise.COLUMN_SET_ID));
    }

    public String getDesc() {return desc;}

    public String getImgPath() {return img_path;}

    public int getSetId(){return set_id;}

    public String getCode(){ return code;}

    public int getId(){ return id;}

    @Override
    public String toString(){
        return String.valueOf(code) + " - " + String.valueOf(desc) + " , " + String.valueOf(set_id);
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SET_ID, set_id);
        cv.put(COLUMN_CODE, code);
        cv.put(COLUMN_DESCRIPTION, desc);
        cv.put(COLUMN_SURPRISE_IMG_PATH, img_path);
        return cv;
    }
}


