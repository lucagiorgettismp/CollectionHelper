package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.ui.mainfragments.ZoomImageFragment;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public abstract class BaseSetDetailFragment extends ZoomImageFragment {
    BaseSetDetailAdapter mAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_set_detail, container, false);

        ProgressBar progress = root.findViewById(R.id.set_detail_loading);
        setProgressBar(progress);

        recyclerView = root.findViewById(R.id.set_detail_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), SystemUtils.getColumnsNumber(recyclerView));
        recyclerView.setLayoutManager(layoutManager);

        setupView();

        recyclerView.setAdapter(mAdapter);

        return root;
    }

    public abstract void setupView();
}
