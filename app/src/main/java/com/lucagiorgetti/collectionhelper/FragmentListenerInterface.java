package com.lucagiorgetti.collectionhelper;

import com.lucagiorgetti.collectionhelper.model.Surprise;

/**
 * Created by Luca on 23/11/2017.
 */

public interface FragmentListenerInterface {
    void setDoublesTitle();

    void onClickOpenProducersFragment();

    void onSwipeRemoveDouble(String id);

    void onSwipeRemoveMissing(String id);

    void onSwipeShowDoublesOwner(Surprise s);

    void setMissingsTitle();

    void onProducerClick(String id, String prod_name);

    void setProducerTitle();

    void onSetShortClick(String id, String name);

    void setSearchTitle();

    void onItemAddMissings(String id);

    void onItemAddDoubles(String id);

    void onHomeClick();

    void setItemsTitle();

    void onYearClicked(String id, int year);

    void setYearTitle();
}
