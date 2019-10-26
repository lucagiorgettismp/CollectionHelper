package com.lucagiorgetti.surprix.ui.setdetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.List;

public class SetDetailViewModel extends BaseViewModel {

    private MutableLiveData<List<Surprise>> allSurprises;

    public SetDetailViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<Surprise>> getSurprises(String setId) {
        if (allSurprises == null) {
            allSurprises = new MutableLiveData<>();
            loadSurprises(setId);
        }

        return allSurprises;
    }

    private void loadSurprises(String setId) {
        DatabaseUtility.getSurprisesBySet(setId, new FirebaseCallback<Surprise>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<Surprise> surprises) {
                allSurprises.setValue(surprises);
                setLoading(false);
            }

            @Override
            public void onFailure() {
                setLoading(false);
            }
        });
    }
}