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

    public DoubleListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<Surprise>> getDoubleSurprises() {
        if (allDoubleSurprises == null) {
            allDoubleSurprises = new MutableLiveData<>();
            loadDoubleSurprises();
        }

        return allDoubleSurprises;
    }


    public void loadDoubleSurprises() {
        DatabaseUtility.getDoublesForUsername(new FirebaseListCallback<Surprise>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<Surprise> doubleSurprises) {
                allDoubleSurprises.setValue(doubleSurprises);
                setLoading(false);
            }

            @Override
            public void onFailure() {
                allDoubleSurprises.setValue(null);
                setLoading(false);
            }
        });
    }

    public void addDouble(Surprise surprise, int position) {
        List<Surprise> list = allDoubleSurprises.getValue();
        list.add(position, surprise);
        allDoubleSurprises.setValue(list);
    }
}