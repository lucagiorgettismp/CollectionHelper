package com.lucagiorgetti.collectionhelper.listenerInterfaces;

import com.lucagiorgetti.collectionhelper.model.Surprise;
import com.lucagiorgetti.collectionhelper.model.User;

/**
 * Interface which contains all methods called from fragments.
 *
 * Created by Luca on 23/11/2017.
 */

public interface FragmentListenerInterface {
    void setDoublesTitle(int number);

    void onClickOpenProducersFragment();

    void onSwipeRemoveDouble(String id);

    void onSwipeRemoveMissing(String id);

    void onSwipeShowDoublesOwner(Surprise s);

    void setMissingsTitle(int number);

    void onProducerClick(String id, String prod_name);

    void setProducerTitle();

    void onSetShortClick(String id, String name);

    void setSearchTitle();

    void onItemAddMissings(String id);

    void onItemAddDoubles(String id);

    void setItemsTitle();

    void onYearClicked(String id, int year);

    void onLongYearClicked(String id, int year);

    void setYearTitle();

    void onSetLongClick(String id, String name);

    User getCurrentRetrievedUser();

    void refreshUser();

    void setSettingsTitle();

    void openChangePwdDialog();

    void openDeleteUserDialog();
}
