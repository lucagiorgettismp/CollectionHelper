package com.lucagiorgetti.surprix.ui.year;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.Collections;
import java.util.List;

public class YearViewModel extends BaseViewModel {

    private MutableLiveData<List<Year>> allYears;

    public YearViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<Year>> getYears(String producerId) {
        if (allYears == null) {
            allYears = new MutableLiveData<>();
            loadYears(producerId);
        }

        return allYears;
    }

    private void loadYears(String producerId) {
        DatabaseUtility.getYearsFromProducer(producerId, new FirebaseListCallback<Year>() {
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
    }
}