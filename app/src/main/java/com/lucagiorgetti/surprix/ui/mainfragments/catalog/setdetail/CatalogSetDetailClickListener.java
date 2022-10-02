package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import android.widget.ImageView;
import com.lucagiorgetti.surprix.model.Surprise;

public interface CatalogSetDetailClickListener extends SetDetailClickListener{
    void onSurpriseAddedToDoubles(Surprise s);

    void onSurpriseAddedToMissings(Surprise s);
}