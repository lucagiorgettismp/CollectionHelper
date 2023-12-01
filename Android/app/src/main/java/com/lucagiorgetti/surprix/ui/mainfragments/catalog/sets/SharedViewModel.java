package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SharedViewModel extends AndroidViewModel {
    MutableLiveData<Boolean> checked = new MutableLiveData<>();
    int position;

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked.setValue(checked);
    }

    public MutableLiveData<Boolean> getChecked() {
        return checked;
    }

    public void setChecked(MutableLiveData<Boolean> checked) {
        this.checked = checked;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
