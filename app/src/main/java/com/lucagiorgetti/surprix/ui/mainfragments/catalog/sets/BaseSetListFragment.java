package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.BaseFragment;

public abstract class BaseSetListFragment extends BaseFragment {
    SetRecyclerAdapter mAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_set_list, container, false);

        mAdapter = setAdapter();
        ProgressBar progress = root.findViewById(R.id.set_loading);
        setProgressBar(progress);

        recyclerView = root.findViewById(R.id.set_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::reloadData);

        setHasOptionsMenu(true);
        setupView();

        return root;
    }

    //protected abstract void loadData();

    protected abstract void reloadData();

    protected abstract void setupView();

    protected abstract SetRecyclerAdapter setAdapter();
}
