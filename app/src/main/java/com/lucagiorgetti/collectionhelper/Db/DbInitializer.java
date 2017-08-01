package com.lucagiorgetti.collectionhelper.Db;

import com.lucagiorgetti.collectionhelper.model.* ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luca Giorgetti on 26/06/2017.
 */

public class DbInitializer{
    private final DbManager mng;
    public DbInitializer (DbManager manager){
        this.mng = manager;
    }
    private List<Year> years = new ArrayList<>();
    private List<Set> sets = new ArrayList<>();
    public void AddSurprises() {
        Year y_2016_1 = new Year(2016, 1);//1
        years.add(y_2016_1);
        Year y_2017_0 = new Year(2017, 0);//2
        years.add(y_2017_0);

        for (Year y : years){mng.addYear(y);}

        Set puffi_2016 = new Set("Puffi", getYearId(y_2016_1) , "", 15);//1
        sets.add(puffi_2016);
        Set teenidols_2017 = new Set("TeenIdols", getYearId(y_2017_0) ,"", 15);//2
        sets.add(teenidols_2017);

        for (Set s : sets){ mng.addSet(s);}

        mng.addSurprise(new Surprise("SD321","Grande Puffo","/sdcard/Mancolista/SD321.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD322","Forzuto","/sdcard/Mancolista/SD322.jpg",getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD323","Tontolone","/sdcard/Mancolista/SD323.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD324","Puffetta","/sdcard/Mancolista/SD324.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD325","Gargamella","/sdcard/Mancolista/SD325.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD327","Quatrocchi","/sdcard/Mancolista/SD327.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD351","Sigrid","/sdcard/Mancolista/SD351.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("SD352","Birba","/sdcard/Mancolista/SD352.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("FT121","Puffetta Variante","/sdcard/Mancolista/FT121.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("FT121","Gargamella Variante","/sdcard/Mancolista/FT128.jpg", getSetId(puffi_2016)));
        mng.addSurprise(new Surprise("TR123","Linsday Vonn","/sdcard/Mancolista/FT128.jpg", getSetId(teenidols_2017)));
    }

    public int getYearId(Year year){
        return years.indexOf(year) + 1;
    }

    public int getSetId(Set set){
        return sets.indexOf(set) + 1;
    }
}


