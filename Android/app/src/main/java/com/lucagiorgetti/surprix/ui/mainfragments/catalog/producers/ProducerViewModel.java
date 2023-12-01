package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.dao.CollectionDao;
import com.lucagiorgetti.surprix.utility.dao.ProducerDao;

import java.util.List;

public class ProducerViewModel extends BaseViewModel {

    private MutableLiveData<List<Producer>> allProducers;

    public ProducerViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<Producer>> getProducers(CatalogNavigationMode mode) {
        if (allProducers == null) {
            allProducers = new MutableLiveData<>();
            loadProducers(mode);
        }

        return allProducers;
    }

    private void loadProducers(CatalogNavigationMode mode) {
        if (mode.equals(CatalogNavigationMode.CATALOG)) {
            ProducerDao.getProducers(new FirebaseListCallback<Producer>() {
                @Override
                public void onStart() {
                    setLoading(true);
                }

                @Override
                public void onSuccess(List<Producer> producers) {
                    allProducers.setValue(producers);
                    setLoading(false);
                }

                @Override
                public void onFailure() {
                    allProducers.setValue(null);
                    setLoading(false);
                }
            });
        } else {
            new CollectionDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getCollectionProducers(new FirebaseListCallback<Producer>() {
                @Override
                public void onStart() {
                    setLoading(true);
                }

                @Override
                public void onSuccess(List<Producer> items) {
                    allProducers.setValue(items);
                    setLoading(false);
                }

                @Override
                public void onFailure() {
                    setLoading(false);
                }
            });
        }
    }
}