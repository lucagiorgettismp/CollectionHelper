package com.lucagiorgetti.surprix.model;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;

/**
 * Created by Luca on 06/11/2017.
 */

public final class Categories {
    public static final String COMPO = "Compo";
    public static final String HANDPAINTED = "Hand_painted";

    private Categories() {
    }

    public static String getDescriptionByString(String value){
        switch (value){
            case COMPO:
                return SurprixApplication.getInstance().getString(R.string.compo);
            case HANDPAINTED:
                return SurprixApplication.getInstance().getString(R.string.handpainted);
        }

        return "Other";
    }
}
