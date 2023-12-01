package com.lucagiorgetti.surprix.ui.mainfragments.missingowners;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MissingOwnersViewModel extends BaseViewModel {
    private MutableLiveData<List<User>> missingOwners;

    public MissingOwnersViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<User>> getMissingOwners(String surpriseId) {
        if (missingOwners == null) {
            missingOwners = new MutableLiveData<>();
            loadOwners(surpriseId);
        }

        return missingOwners;
    }

    void loadOwners(String surpriseId) {
        new DoubleListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getMissingOwners(surpriseId, new FirebaseListCallback<User>() {
            @Override
            public void onSuccess(List<User> users) {
                final ArrayList<User> owners = new ArrayList<>();
                final ArrayList<User> abroad_owners = new ArrayList<>();

                if (users != null){
                    for (User u : users) {
                        if (Objects.equals(u.getCountry(), SurprixApplication.getInstance().getCurrentUser().getCountry())) {
                            owners.add(u);
                        } else {
                            abroad_owners.add(u);
                        }
                    }
                }

                owners.addAll(abroad_owners);
                missingOwners.setValue(owners);
                setLoading(false);
            }

            @Override
            public void onStart() {
                setLoading(true);
            }

            @Override
            public void onFailure() {
                setLoading(false);
            }
        });

    }
}
