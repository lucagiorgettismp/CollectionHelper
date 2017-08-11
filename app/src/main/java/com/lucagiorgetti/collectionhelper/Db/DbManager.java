package com.lucagiorgetti.collectionhelper.Db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lucagiorgetti.collectionhelper.model.*;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private final DbHelper dbHelper;

    public DbManager(Context context) {
 //       context.deleteDatabase("database.db");
        dbHelper = new DbHelper(context);
    }

    public boolean addSurprise(Surprise surprise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Surprise.TABLE_NAME, null, surprise.getContentValues());
        return row > 1;
    }
    public boolean addSet(Set set) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Set.TABLE_NAME, null, set.getContentValues());
        return row > 1;
    }
    public boolean addYear(Year year) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Year.TABLE_NAME, null, year.getContentValues());
        return row > 1;
    }
    public boolean addProducer(Producer producer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Producer.TABLE_NAME, null, producer.getContentValues());
        return row > 1;
    }



    public boolean deleteSurprise(Surprise surprise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Surprise.TABLE_NAME, Surprise._ID + " = ?", new String[]{Integer.toString(surprise.getId())});
        return row > 0;
    }
    public boolean deleteSet(Set set) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Set.TABLE_NAME, Set._ID + " = ?", new String[]{Integer.toString(set.getId())});
        return row > 0;
    }
    public boolean deleteYear(Year year) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Year.TABLE_NAME, Year._ID + " = ?", new String[]{Integer.toString(year.getId())});
        return row > 0;
    }

    public ArrayList<Surprise> getSurprises() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Surprise> surprises = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Surprise.TABLE_NAME +
                    " ORDER BY " + Surprise.COLUMN_CODE + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Surprise surprise = new Surprise(cursor);
                surprises.add(surprise);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return surprises;
    }
    public ArrayList<Set> getSets() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Set> sets = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Set.TABLE_NAME +
                    " ORDER BY " + Set._ID + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Set set = new Set(cursor);
                sets.add(set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return sets;
    }
    public List<Year> getYears() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Year> years = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Year.TABLE_NAME +
                    " ORDER BY " + Year.COLUMN_YEAR_NUMBER + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Year year = new Year(cursor);
                years.add(year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return years;
    }
    public List<Producer> getProducers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Producer> producers = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Producer.TABLE_NAME +
                    " ORDER BY " + Producer.COLUMN_PRODUCER_NAME + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Producer producer = new Producer(cursor);
                producers.add(producer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return producers;
    }

    public Set getSetById(int id){
        List<Set> sets = getSets();
        Set set = null;
        for (Set s : sets) {
            if (s.getId() == id) {
                set = s;
            }
        }
        return set;
    }
    public Year getYearById(int id){
        Year year = null;
        for (Year y : this.getYears())
            if (y.getId() == id) {
                year = y;
            }
        return year;
    }
    public Producer getProducerById (int id){
        Producer producer = null;
        for (Producer p : this.getProducers())
            if (p.getId() == id) {
                producer = p;
            }
        return producer;
    }

    public boolean getExistingUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + User.TABLE_NAME +
                    " ORDER BY " + User.COLUMN_USER_NAME + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                User user = new User(cursor);
                //ritorno true se ho un utente con lo stesso username
                if(user.getUsername() == username){
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        // nel caso non abbia trovato nessun utente con lo stesso username.
        return false;
    }
}
