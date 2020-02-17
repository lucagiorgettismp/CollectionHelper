package com.lucagiorgetti.surprix.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> loading;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        loading = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading.setValue(loading);
    }
}
