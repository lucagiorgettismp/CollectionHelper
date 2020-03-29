package com.lucagiorgetti.surprix.ui.mainfragments.setlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;

import java.util.List;

public class SetListViewModel extends BaseViewModel {

    private MutableLiveData<List<Set>> allSets;

    public SetListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<Set>> getSets(String setId) {
        if (allSets == null) {
            allSets = new MutableLiveData<>();
            loadSets(setId);
        }

        return allSets;
    }

    private void loadSets(String yearId) {
        DatabaseUtils.getSetsFromYear(yearId, new FirebaseListCallback<Set>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<Set> sets) {
                allSets.setValue(sets);
                setLoading(false);
            }

            @Override
            public void onFailure() {
                setLoading(false);
            }
        });
    }
}