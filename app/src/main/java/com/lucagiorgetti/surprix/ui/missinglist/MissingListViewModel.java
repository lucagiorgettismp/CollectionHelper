package com.lucagiorgetti.surprix.ui.missinglist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.MissingPresenter;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.List;

public class MissingListViewModel extends BaseViewModel {

    private MutableLiveData<List<MissingPresenter>> allMissingSurprises;

    public MissingListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<MissingPresenter>> getMissingSurprises() {
        if (allMissingSurprises == null) {
            allMissingSurprises = new MutableLiveData<>();
            loadMissingSurprises();
        }

        return allMissingSurprises;
    }

    public void loadMissingSurprises() {
        DatabaseUtility.getMissingsForUsername(new FirebaseListCallback<MissingPresenter>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<MissingPresenter> missingSurprises) {
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

    public void addMissing(MissingPresenter mp, int position) {
        List<MissingPresenter> list = allMissingSurprises.getValue();
        list.add(position, mp);
        allMissingSurprises.setValue(list);
    }
}