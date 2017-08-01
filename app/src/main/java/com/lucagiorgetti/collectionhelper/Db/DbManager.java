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

    public boolean updateSurprise(Surprise surprise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(Surprise.TABLE_NAME, surprise.getContentValues(),
                Surprise._ID + " = ? ", new String[]{Integer.toString(surprise.getId())});
        return row > 0;
    }
    public boolean updateSet(Set set) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(Set.TABLE_NAME, set.getContentValues(),
                Surprise._ID + " = ? ", new String[]{Integer.toString(set.getId())});
        return row > 0;
    }
    public boolean updateYear(Year year) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(Year.TABLE_NAME, year.getContentValues(),
                Surprise._ID + " = ? ", new String[]{Integer.toString(year.getId())});
        return row > 0;
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
    public List<Set> getSets() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Set> sets = new ArrayList<>();

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
}
