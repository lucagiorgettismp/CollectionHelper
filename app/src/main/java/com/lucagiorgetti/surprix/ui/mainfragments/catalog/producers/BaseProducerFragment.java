package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.BaseFragment;

public abstract class BaseProducerFragment extends BaseFragment {
    ProducerRecyclerAdapter mAdapter;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        ProgressBar progress = root.findViewById(R.id.catalog_loading);
        setProgressBar(progress);

        fab = root.findViewById(R.id.catalog_fab_search);

        recyclerView = root.findViewById(R.id.catalog_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProducerRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        setupView();

        return root;
    }

    public abstract void setupView();
}
