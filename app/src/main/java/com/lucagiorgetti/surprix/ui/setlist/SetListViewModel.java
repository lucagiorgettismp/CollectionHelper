package com.lucagiorgetti.surprix.ui.setlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.model.Categories;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.ArrayList;
import java.util.List;

public class SetListViewModel extends BaseViewModel {

    private MutableLiveData<List<Set>> allSets;

    public SetListViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    public MutableLiveData<List<Set>> getSets(String setId) {
        if (allSets == null) {
            allSets = new MutableLiveData<>();
            loadSets(setId);
        }

        return allSets;
    }

    private void loadSets(String yearId) {
        DatabaseUtility.getSetsFromYear(yearId, new FirebaseCallback<Set>() {
            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onSuccess(List<Set> sets) {
                ArrayList<Set> handpaintedSets = new ArrayList<>();
                ArrayList<Set> compoSets = new ArrayList<>();

                if (sets != null) {
                    for (Set s : sets) {
                        if (s.getCategory().equals(Categories.HANDPAINTED)) {
                            handpaintedSets.add(s);
                        } else {
                            compoSets.add(s);
                        }
                    }
                }

                handpaintedSets.addAll(compoSets);
                sets = handpaintedSets;

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