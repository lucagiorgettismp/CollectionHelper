package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.dao.CollectionDao;
import com.lucagiorgetti.surprix.utility.dao.YearDao;

import java.util.List;
import java.util.stream.Collectors;

import timber.log.Timber;

public class SetListViewModel extends BaseViewModel {

    private MutableLiveData<List<CatalogSet>> allSets;

    public SetListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<CatalogSet>> getSets(String yearId,  String producerId, CatalogNavigationMode mode) {
        if (allSets == null) {
            allSets = new MutableLiveData<>();
            loadSets(yearId, producerId, mode);
        }

        return allSets;
    }

    private void loadSets(String yearId,  String producerId, CatalogNavigationMode mode) {
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
            new CollectionDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getCollectionSets(producerId, yearId, new FirebaseListCallback<Set>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(List<Set> items) {
                    allSets.setValue(items.stream().map(CatalogSet::new).collect(Collectors.toList()));
                    setLoading(false);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
}