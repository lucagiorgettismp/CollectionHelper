package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

import java.util.List;

public class MissingListViewModel extends BaseViewModel {

    private MutableLiveData<List<Surprise>> allMissingSurprises;

    public MissingListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<Surprise>> getMissingSurprises() {
        if (allMissingSurprises == null) {
            allMissingSurprises = new MutableLiveData<>();
            loadMissingSurprises();
        }

        return allMissingSurprises;
    }

    void loadMissingSurprises() {
        new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getMissingList(new FirebaseListCallback<Surprise>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<Surprise> missingSurprises) {
                allMissingSurprises.setValue(missingSurprises);
                setLoading(false);
            }

            @Override
            public void onFailure() {
                allMissingSurprises.setValue(null);
                setLoading(false);
            }
        });
    }

    public void addMissing(Surprise missingSurprise, int position) {
        List<Surprise> list = allMissingSurprises.getValue();
        list.add(missingSurprise);
        allMissingSurprises.setValue(list);
    }
}