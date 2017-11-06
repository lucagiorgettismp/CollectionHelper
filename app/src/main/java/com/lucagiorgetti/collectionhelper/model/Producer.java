package com.lucagiorgetti.collectionhelper.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Producer {
    private String name = null;

    public Producer(String name){
        this.name = name;
    }

    public Producer(){

    }

    public String getName() {
        return name;
    }
}