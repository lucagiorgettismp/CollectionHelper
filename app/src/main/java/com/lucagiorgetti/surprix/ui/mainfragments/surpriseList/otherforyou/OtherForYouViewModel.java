package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.otherforyou;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.BaseViewModel;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

import java.util.List;

public class OtherForYouViewModel extends BaseViewModel {
    private MutableLiveData<List<Surprise>> otherForYou;

    public OtherForYouViewModel(@NonNull Application application) {
        super(application);
        this.setLoading(false);
    }

    MutableLiveData<List<Surprise>> getOtherForYou(String username) {
        if (otherForYou == null) {
            otherForYou = new MutableLiveData<>();
            loadOtherForYou(username);
        }

        return otherForYou;
    }

    void loadOtherForYou(String username) {
        new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).getMissingOwnerOtherSurprises(username, new FirebaseListCallback<Surprise>() {
            @Override
            public void onSuccess(List<Surprise> surprises) {
                otherForYou.setValue(surprises);
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
