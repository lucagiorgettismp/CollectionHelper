package com.lucagiorgetti.surprix.ui.doublelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SurpRecyclerAdapter;

public class DoubleListFragment extends Fragment {

    private DoubleListViewModel doubleListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doubleListViewModel =
                ViewModelProviders.of(this).get(DoubleListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_double_list, container, false);
        View emptyList = root.findViewById(R.id.double_empty_list);
        ProgressBar progress = root.findViewById(R.id.double_loading);
        RecyclerView recyclerView = root.findViewById(R.id.double_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        SurpRecyclerAdapter mAdapter = new SurpRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        doubleListViewModel.getDoubleSurprises().observe(this, doubleList -> {
            emptyList.setVisibility(doubleList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(doubleList);
        });

        doubleListViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        return root;
    }
}