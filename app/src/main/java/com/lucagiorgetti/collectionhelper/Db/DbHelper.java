package com.lucagiorgetti.collectionhelper.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lucagiorgetti.collectionhelper.model.*;
import com.lucagiorgetti.collectionhelper.model.Double;

/**
 * @author Elia Di Pasquale
 */

public class DbHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    //Dati del database
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    //Riferimenti SQL alle costanti per creare tabelle di database
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";
    public static final String ID = "(_ID)";
    public static final String REFERENCE = " REFERENCES ";
    public static final String INTEGER_PRIMARY_KEY = INTEGER_TYPE + " PRIMARY KEY";
    public static final String FOREIGN_KEY = " FOREIGN KEY ";

    /**
     * Costruttore che richiama il costruttore padre, passando il riferimento al contesto, il nome del database e
     * la versione del database.
     *
     * @param context riferimento al contesto
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        Log.w("BUILDER", "builded");
    }

    //Query per la creazione delle tabelle, sfruttando i nomi delle colonne delle classi del modello.

    //Surprise
    public static final String CREATE_TABLE_SURPRISE = "CREATE TABLE " + Surprise.TABLE_NAME +
            " (" +
            Surprise._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Surprise.COLUMN_CODE + INTEGER_TYPE + COMMA_SEP +
            Surprise.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            Surprise.COLUMN_SURPRISE_IMG_PATH + TEXT_TYPE + COMMA_SEP +
            Surprise.COLUMN_SET_ID + INTEGER_TYPE + COMMA_SEP +
            FOREIGN_KEY + " ( " + Surprise.COLUMN_SET_ID + " ) " + REFERENCE + Set.TABLE_NAME + ID +
            " )";

    //Set
    public static final String CREATE_TABLE_SET = "CREATE TABLE " + Set.TABLE_NAME +
            " (" +
            Set._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Set.COLUMN_SET_ID + INTEGER_TYPE + COMMA_SEP +
            Set.COLUMN_SET_NAME + TEXT_TYPE + COMMA_SEP +
            Set.COLUMN_SET_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
            Set.COLUMN_YEAR_ID + INTEGER_TYPE + COMMA_SEP +
            Set.COLUMN_PRODUCER_ID + INTEGER_TYPE + COMMA_SEP +
            FOREIGN_KEY + " ( " + Set.COLUMN_YEAR_ID + " ) " + REFERENCE + Year.TABLE_NAME + ID + COMMA_SEP +
            FOREIGN_KEY + " ( " + Set.COLUMN_PRODUCER_ID + " ) " + REFERENCE + Producer.TABLE_NAME + ID +
            " )";

    //Year
    public static final String CREATE_TABLE_YEAR = "CREATE TABLE " + Year.TABLE_NAME +
            " (" +
            Year._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Year.COLUMN_YEAR_ID + INTEGER_TYPE + COMMA_SEP +
            Year.COLUMN_YEAR_NUMBER + INTEGER_TYPE + COMMA_SEP +
            Year.COLUMN_YEAR_SEASON + INTEGER_TYPE +
            " )";

    //Producer
    public static final String CREATE_TABLE_PRODUCR = "CREATE TABLE " + Producer.TABLE_NAME +
            " (" +
            Producer._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Producer.COLUMN_PRODUCER_ID + INTEGER_TYPE + COMMA_SEP +
            Producer.COLUMN_PRODUCER_NAME + TEXT_TYPE + COMMA_SEP +
            Producer.COLUMN_PRODUCER_NATION + TEXT_TYPE +
            " )";

    //Missing
    public static final String CREATE_TABLE_MISSING = "CREATE TABLE " + Missing.TABLE_NAME +
            " (" +
            Missing._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Missing.COLUMN_MISSING_ID + INTEGER_TYPE + COMMA_SEP +
            Missing.COLUMN_USER_ID + INTEGER_TYPE + COMMA_SEP +
            Missing.COLUMN_SURPRISE_ID + INTEGER_TYPE + COMMA_SEP +
            FOREIGN_KEY + " ( " + Missing.COLUMN_USER_ID + " ) " + REFERENCE + User.TABLE_NAME + ID + COMMA_SEP +
            FOREIGN_KEY + " ( " + Missing.COLUMN_SURPRISE_ID + " ) " + REFERENCE + Surprise.TABLE_NAME + ID +
            " )";

    //Double
    public static final String CREATE_TABLE_DOUBLE = "CREATE TABLE " + Double.TABLE_NAME +
            " (" +
            Double._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Double.COLUMN_DOUBLE_ID + INTEGER_TYPE + COMMA_SEP +
            Double.COLUMN_USER_ID + INTEGER_TYPE + COMMA_SEP +
            Double.COLUMN_SURPRISE_ID + INTEGER_TYPE + COMMA_SEP +
            FOREIGN_KEY + " ( " + Double.COLUMN_USER_ID + " ) " + REFERENCE + User.TABLE_NAME + ID + COMMA_SEP +
            FOREIGN_KEY + " ( " + Double.COLUMN_SURPRISE_ID + " ) " + REFERENCE + Double.TABLE_NAME + ID +
            " )";

    //User
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + User.TABLE_NAME +
            " (" +
            User._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            User.COLUMN_USER_ID + INTEGER_TYPE + COMMA_SEP +
            User.COLUMN_USER_NAME + TEXT_TYPE + COMMA_SEP +
            User.COLUMN_USER_SURNAME + TEXT_TYPE + COMMA_SEP +
            User.COLUMN_USER_USERNAME + TEXT_TYPE + COMMA_SEP +
            User.COLUMN_USER_PASSWORD + TEXT_TYPE + COMMA_SEP +
            User.COLUMN_USER_EMAIL + TEXT_TYPE + COMMA_SEP +
            User.COLUMN_USER_BIRTH_DATE + TEXT_TYPE + COMMA_SEP +
            User.COLUMN_USER_NATION + TEXT_TYPE +
            " )";

    /**
     * Metodo da overridare per aver esteso SQLiteOpenHelper; viene richiamato alla prima creazione del database.
     * In questo metodo, tramite il riferimento al database, vengono eseguite le query per la creazione delle tabelle
     * che comporranno il database.
     *
     * @param db riferimento al database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_YEAR);
        db.execSQL(CREATE_TABLE_SET);
        db.execSQL(CREATE_TABLE_SURPRISE);
        db.execSQL(CREATE_TABLE_PRODUCR);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MISSING);
        db.execSQL(CREATE_TABLE_DOUBLE);
    }


    /**
     * Metodo da overridare per aver esteso SQLiteOpenHelper; viene richiamato ogni volta che viene cambiata la versione del database.
     * In questo metodo vengono effettuate le modifiche alla struttura del database, come l'aggiunta di nuove tabelle o la modifica
     * di quelle esistenti.
     *
     * @param db riferimento al database
     * @param oldVersion versione del database sul dispositivo
     * @param newVersion nuova versione del database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
