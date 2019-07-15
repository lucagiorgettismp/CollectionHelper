package com.lucagiorgetti.surprix.listenerInterfaces;

import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;

/**
 * Interface which contains all methods called from fragments.
 * <p>
 * Created by Luca on 23/11/2017.
 */

public interface FragmentListenerInterface {
    void setDoublesTitle(int number);

    void onClickOpenProducers();

    void onSwipeRemoveDouble(String id);

    void onSwipeRemoveMissing(String id);

    void onSwipeShowDoublesOwner(Surprise s);

    void setMissingsTitle(int number);

    User getCurrentRetrievedUser();

    void refreshUser();

    void setSettingsTitle();

    void openChangePwdDialog();

    void openDeleteUserDialog();

    void onBannerClicked(String url);

    void setThanksToTitle();
}
