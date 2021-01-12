package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.missinglist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

import java.util.ArrayList;
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
        List<Surprise> surprises = new ArrayList<>();
        new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getMissingList(new CallbackInterface<Surprise>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(Surprise missingSurprise) {
                surprises.add(missingSurprise);
                allMissingSurprises.setValue(surprises);
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