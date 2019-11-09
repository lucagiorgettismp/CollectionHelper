package com.lucagiorgetti.surprix.ui.setdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetDetailRecyclerAdapter;

public class SetDetailFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SetDetailViewModel setDetailViewModel = ViewModelProviders.of(this).get(SetDetailViewModel.class);

        View root = inflater.inflate(R.layout.fragment_set_detail, container, false);

        SetDetailRecyclerAdapter mAdapter;
        ProgressBar progress = root.findViewById(R.id.set_detail_loading);
        RecyclerView recyclerView = root.findViewById(R.id.set_detail_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SetDetailRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        String setId = null;
        if (getArguments() != null) {
            setId = getArguments().getString("set_id");
        }

        setDetailViewModel.getSurprises(setId).observe(this, surprises -> {
            mAdapter.setSurprises(surprises);
            mAdapter.notifyDataSetChanged();
        });

        setDetailViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}