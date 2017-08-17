package com.lucagiorgetti.collectionhelper.Db;

import android.util.Log;

import com.lucagiorgetti.collectionhelper.model.* ;

import java.util.ArrayList;
import java.util.List;

import static com.lucagiorgetti.collectionhelper.R.layout.sets;

/**
 * Created by Luca Giorgetti on 26/06/2017.
 */

public class DbInitializer{
    private final DbManager mng;
    public DbInitializer (DbManager manager){
        this.mng = manager;
    }/*
    private List<Surprise> surprises = new ArrayList<>();
    private List<Year> years = new ArrayList<>();
    private List<Set> sets = new ArrayList<>();
    private List<Producer> producers = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Missing> missings = new ArrayList<>();
    */
    public void AddSurprises() {

        mng.addUser(new User("Guest", "Guest", "asd@asd.it", "asd", "asd", "25/01/1995", "Italia", mng.getNewUserId())); //0

        mng.addProducer(new Producer("Kinder", "Italia", mng.getNewProducerId())); //0

        mng.addYear(new Year(2016, 1, mng.getNewYearId())); //0
        mng.addYear(new Year(2017, 0, mng.getNewYearId())); //1

        mng.addSet(new Set("Puffi", 0, "", 0, mng.getNewSetId())); //0
        mng.addSet(new Set("Teen Idols", 1, "", 0, mng.getNewSetId())); //1

        mng.addSurprise(new Surprise("SD321","Grande Puffo","/sdcard/Mancolista/SD321.jpg", 0));
        mng.addSurprise(new Surprise("SD322","Forzuto","/sdcard/Mancolista/SD322.jpg", 0));
        mng.addSurprise(new Surprise("SD323","Tontolone","/sdcard/Mancolista/SD323.jpg", 0));
        mng.addSurprise(new Surprise("SD324","Puffetta","/sdcard/Mancolista/SD324.jpg", 0));
        mng.addSurprise(new Surprise("SD325","Gargamella","/sdcard/Mancolista/SD325.jpg", 0));
        mng.addSurprise(new Surprise("SD327","Quatrocchi","/sdcard/Mancolista/SD327.jpg", 0));
        mng.addSurprise(new Surprise("SD351","Sigrid","/sdcard/Mancolista/SD351.jpg", 0));
        mng.addSurprise(new Surprise("SD352","Birba","/sdcard/Mancolista/SD352.jpg", 0));
        mng.addSurprise(new Surprise("FT121","Puffetta Variante","/sdcard/Mancolista/FT121.jpg", 0));
        mng.addSurprise(new Surprise("FT122","Gargamella Variante","/sdcard/Mancolista/FT128.jpg", 0));
        mng.addSurprise(new Surprise("TR123","Linsday Vonn","/sdcard/Mancolista/FT128.jpg", 1));

        mng.addMissing(new Missing(0, "SD325", mng.getNewMissingId()));
        mng.addMissing(new Missing(0, "SD322", mng.getNewMissingId()));
    }

}


