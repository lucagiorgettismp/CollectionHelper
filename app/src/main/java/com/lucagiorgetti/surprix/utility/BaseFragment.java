package com.lucagiorgetti.surprix.utility;

import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.lucagiorgetti.surprix.ui.activities.MainActivity;

import java.util.Objects;

public class BaseFragment extends Fragment {
    private ProgressBar progressBar;

    public void setTitle(String title){
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(title);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        progressBar.setVisibility(View.INVISIBLE);
    }
}
