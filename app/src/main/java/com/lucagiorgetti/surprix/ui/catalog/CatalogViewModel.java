package com.lucagiorgetti.surprix.ui.catalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.List;

public class CatalogViewModel extends BaseViewModel {

    private MutableLiveData<List<Producer>> allProducers;

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<Producer>> getProducers() {
        if (allProducers == null) {
            allProducers = new MutableLiveData<>();
            loadProducers();
        }

        return allProducers;
    }

    private void loadProducers() {
        DatabaseUtility.getProducers(new FirebaseCallback<Producer>() {
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
                setLoading(false);
            }
        });
    }
}