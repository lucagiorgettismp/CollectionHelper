package com.lucagiorgetti.surprix.ui.missinglist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.List;

public class MissingListViewModel extends BaseViewModel {

    private MutableLiveData<List<MissingSurprise>> allMissingSurprises;

    public MissingListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<MissingSurprise>> getMissingSurprises() {
        if (allMissingSurprises == null) {
            allMissingSurprises = new MutableLiveData<>();
            loadMissingSurprises();
        }

        return allMissingSurprises;
    }

    public void loadMissingSurprises() {
        DatabaseUtility.getMissingsForUsername(new FirebaseListCallback<MissingSurprise>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<MissingSurprise> missingSurprises) {
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

    public void addMissing(MissingSurprise mp, int position) {
        List<MissingSurprise> list = allMissingSurprises.getValue();
        list.add(position, mp);
        allMissingSurprises.setValue(list);
    }
}