package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.dao.CollectionDao;
import com.lucagiorgetti.surprix.utility.dao.YearDao;

import java.util.List;

public class SetListViewModel extends BaseViewModel {

    private MutableLiveData<List<CatalogSet>> allSets;

    public SetListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<CatalogSet>> getSets(String yearId, String producerId, CatalogNavigationMode mode) {
        if (allSets == null) {
            allSets = new MutableLiveData<>();
            loadSets(yearId, producerId, mode);
        }

        return allSets;
    }

    private void loadSets(String yearId, String producerId, CatalogNavigationMode mode) {
        if (mode.equals(CatalogNavigationMode.CATALOG)) {
            YearDao.getYearCatalogSets(yearId, new FirebaseListCallback<CatalogSet>() {
                @Override
                public void onStart() {
                    setLoading(true);
                }

                @Override
                public void onSuccess(List<CatalogSet> sets) {
                    allSets.setValue(sets);
                    setLoading(false);
                }

                @Override
                public void onFailure() {
                    setLoading(false);
                }
            });
        } else {
            new CollectionDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getCollectionSets(producerId, yearId, new FirebaseListCallback<CatalogSet>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(List<CatalogSet> items) {
                    allSets.setValue(items);
                    setLoading(false);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
}