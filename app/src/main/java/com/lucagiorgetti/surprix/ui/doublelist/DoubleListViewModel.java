package com.lucagiorgetti.surprix.ui.doublelist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.List;

public class DoubleListViewModel extends BaseViewModel {

    private MutableLiveData<List<Surprise>> allDoubleSurprises;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public DoubleListViewModel(@NonNull Application application) {
        super(application);
        this.loading.setValue(false);
    }

    public MutableLiveData<List<Surprise>> getDoubleSurprises() {
        if (allDoubleSurprises == null) {
            allDoubleSurprises = new MutableLiveData<>();
            loadDoubleSurprises();
        }

        return allDoubleSurprises;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }

    public void loadDoubleSurprises() {
        DatabaseUtility.getDoublesForUsername(new FirebaseListCallback<Surprise>() {
            @Override
            public void onStart() {
                loading.setValue(true);
            }

            @Override
            public void onSuccess(List<Surprise> doubleSurprises) {
                allDoubleSurprises.setValue(doubleSurprises);
                loading.setValue(false);
            }

            @Override
            public void onFailure() {
                allDoubleSurprises.setValue(null);
                loading.setValue(false);
            }
        });
    }
}