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
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public abstract class BaseSetDetailFragment extends BaseFragment {
    BaseSetDetailAdapter mAdapter;
    RecyclerView recyclerView;
    private Animator currentAnimator = null;

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

    public void zoomImageFromThumb(String path, ImageView imageView, int placeHolderId){
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        final ImageView expandedImageView = getView().findViewById(R.id.surp_image_expanded);
        final ConstraintLayout imageContainer = getView().findViewById(R.id.surp_expanded_container);
        SystemUtils.loadImage(path, expandedImageView, placeHolderId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        imageView.getGlobalVisibleRect(startBounds);
        getView().findViewById(R.id.surp_expanded_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        imageView.setAlpha(0f);
        imageContainer.setVisibility(View.VISIBLE);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setAlpha(1f);
                imageContainer.setVisibility(View.GONE);
                expandedImageView.setVisibility(View.GONE);
            }
        });
    }
}
