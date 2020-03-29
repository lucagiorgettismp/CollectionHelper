package com.lucagiorgetti.surprix.utility;

import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.lucagiorgetti.surprix.ui.activities.MainActivity;

import java.util.Objects;

public class BaseFragment extends Fragment {
    private ProgressBar progressBar;

    protected void setTitle(String title){
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(title);
    }

    protected void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    protected void showLoading(){
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoading(){
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
