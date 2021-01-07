package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.dao.ProducerDao;

import java.util.ArrayList;
import java.util.List;

public class YearViewModel extends BaseViewModel {

    private MutableLiveData<List<Year>> allYears;

    public YearViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<Year>> getYears(String producerId, CatalogNavigationMode mode) {
        if (allYears == null) {
            allYears = new MutableLiveData<>();
            loadYears(producerId, mode);
        }

        return allYears;
    }

    private void loadYears(String producerId, CatalogNavigationMode mode) {
        //if (mode.equals(CatalogNavigationMode.CATALOG)) {
            ProducerDao.getProducerYears(producerId, new FirebaseListCallback<Year>() {
                @Override
                public void onStart() {
                    setLoading(true);
                }

                @Override
                public void onSuccess(List<Year> years) {
                    allYears.setValue(years);
                    setLoading(false);
                }

                @Override
                public void onFailure() {
                    setLoading(false);
                }
            });
        /*} else {
            allYears.setValue(new ArrayList<>());
            setLoading(false);
        }*/

    }
}