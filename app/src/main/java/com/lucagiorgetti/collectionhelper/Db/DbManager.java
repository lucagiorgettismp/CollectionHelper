package com.lucagiorgetti.collectionhelper.Db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lucagiorgetti.collectionhelper.model.*;
import com.lucagiorgetti.collectionhelper.model.Double;

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
    public boolean addUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(User.TABLE_NAME, null, user.getContentValues());
        return row > 1;
    }
    public boolean addMissing(Missing missing){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Missing.TABLE_NAME, null, missing.getContentValues());
        return row > 1;
    }

    public boolean deleteSurprise(Surprise surprise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Surprise.TABLE_NAME, Surprise._ID + " = ?", new String[]{Integer.toString(surprise.getId())});
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
    public List<User> getUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<User> users = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + User.TABLE_NAME +
                    " ORDER BY " + User.COLUMN_USER_USERNAME + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                User user = new User(cursor);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return users;
    }
    public List<Missing> getMissings(int userId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Missing> missings = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Missing.TABLE_NAME +
                    " ORDER BY " + Missing.COLUMN_SURPRISE_ID + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Missing missing = new Missing(cursor);
                if(missing.getUserId() == userId) {
                    missings.add(missing);
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
        return missings;
    }

    public Set getSetById(int id){
        List<Set> sets = getSets();
        Set set = null;
        for (Set s : sets) {
            if (s.getSetId() == id) {
                set = s;
            }
        }
        return set;
    }
    public Year getYearById(int id){
        Year year = null;
        for (Year y : this.getYears())
            if (y.getYearId() == id) {
                year = y;
            }
        return year;
    }
    public Producer getProducerById (int id){
        Producer producer = null;
        for (Producer p : this.getProducers())
            if (p.getProducerId() == id) {
                producer = p;
            }
        return producer;
    }
    public User getUserById (int id){
        User user = null;
        for (User u : this.getUsers())
            if (user.getUserId() == id) {
                user = u;
            }
        return user;
    }

    public int getNewUserId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + User.TABLE_NAME +
                    " ORDER BY " + User.COLUMN_USER_ID + " DESC" + " LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                User user = new User(cursor);
                int id = user.getUserId();
                int new_id = id +1;
                return new_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
    public int getNewSetId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Set.TABLE_NAME +
                    " ORDER BY " + Set.COLUMN_SET_ID + " DESC" + " LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Set set = new Set(cursor);
                int id = set.getSetId();
                int new_id = id +1;
                return new_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
    public int getNewYearId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Year.TABLE_NAME +
                    " ORDER BY " + Year.COLUMN_YEAR_ID + " DESC" + " LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Year year = new Year(cursor);
                int id = year.getYearId();
                int new_id = id +1;
                return new_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
    public int getNewProducerId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Producer.TABLE_NAME +
                    " ORDER BY " + Producer.COLUMN_PRODUCER_ID + " DESC" + " LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Producer producer = new Producer(cursor);
                int id = producer.getProducerId();
                return id++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
    public int getNewMissingId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Missing.TABLE_NAME +
                    " ORDER BY " + Missing.COLUMN_MISSING_ID + " DESC" + " LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Missing missing = new Missing(cursor);
                int id = missing.getUserId();
                int new_id = id +1;
                return new_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
    public int getNewDoubleId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Double.TABLE_NAME +
                    " ORDER BY " + Double.COLUMN_DOUBLE_ID + " DESC" + " LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Double d = new Double(cursor);
                int id = d.getDoubleId();
                int new_id = id +1;
                return new_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }

    public boolean getExistingUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean finded = false;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + User.TABLE_NAME +
                    " ORDER BY " + User.COLUMN_USER_USERNAME + " ASC";
            cursor = db.rawQuery(query, null);
            ArrayList<User> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                User user = new User(cursor);
                list.add(user);
                if(user.getUsername().equals(username)){
                    finded = true;
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
        return finded;
    }

    public int login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int userId = -1;
        try {
            String query = "SELECT * FROM " + User.TABLE_NAME +
                    " ORDER BY " + User.COLUMN_USER_NAME + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                User user = new User(cursor);
                if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                    userId = user.getUserId();
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
        return userId;
    }

    public Surprise getSurpriseByCode(String code){
        List<Surprise> surprises = getSurprises();
        Surprise surp = null;
        for (Surprise s : surprises) {
            if (s.getCode().equals(code)) {
                surp = s;
            }
        }
        return surp;
    }
}
