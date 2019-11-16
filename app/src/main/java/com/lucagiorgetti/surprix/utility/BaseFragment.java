package com.lucagiorgetti.surprix.utility;

import androidx.fragment.app.Fragment;

import com.lucagiorgetti.surprix.ui.activities.MainActivity;

import java.util.Objects;

public class BaseFragment extends Fragment {
    public void setTitle(String title){
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(title);
    }
}
