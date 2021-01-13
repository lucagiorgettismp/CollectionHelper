package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.BaseFragment;

public abstract class BaseYearFragment extends BaseFragment {
    RecyclerView recyclerView;
    YearRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_years, container, false);
        ProgressBar progress = root.findViewById(R.id.year_loading);
        setProgressBar(progress);

        recyclerView = root.findViewById(R.id.year_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new YearRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        setupView();

        return root;
    }

    public abstract void setupView();
}
