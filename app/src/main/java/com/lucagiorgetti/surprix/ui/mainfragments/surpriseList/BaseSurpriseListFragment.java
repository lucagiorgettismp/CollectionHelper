package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetListener;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.SurpriseFilterBSDFragment;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public abstract class BaseSurpriseListFragment extends BaseFragment {
    public SurpriseRecyclerAdapter mAdapter;
    public SearchView searchView;
    public View root;
    public View emptyList;
    public ChipFilters chipFilters;
    public SwipeRefreshLayout swipeRefreshLayout;
    private Animator currentAnimator = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_surprise_list, container, false);
        }

        emptyList = root.findViewById(R.id.double_empty_list);
        ProgressBar progress = root.findViewById(R.id.double_loading);
        setProgressBar(progress);
        RecyclerView recyclerView = root.findViewById(R.id.double_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), SystemUtils.getColumnsNumber(recyclerView));
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            emptyList.setVisibility(View.GONE);
            loadData();
        });

        setupData();

        recyclerView.setAdapter(mAdapter);

        initSwipe(recyclerView);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openBottomSheet();
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void deleteSurprise(SurpriseRecyclerAdapter mAdapter, int position);

    public abstract void setupData();

    public abstract void loadData();

    private void openBottomSheet() {
        if (chipFilters != null) {
            SurpriseFilterBSDFragment bottomSheetDialogFragment = new SurpriseFilterBSDFragment(this.chipFilters);
            bottomSheetDialogFragment.setListener(new FilterBottomSheetListener() {
                @Override
                public void onFilterChanged(ChipFilters selection) {
                    mAdapter.setChipFilters(selection);
                }

                @Override
                public void onFilterCleared() {
                    chipFilters.clearSelection();
                    mAdapter.setChipFilters(chipFilters);
                    bottomSheetDialogFragment.dismissAllowingStateLoss();
                }
            });
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "surprise_list_bottom_sheet");
        } else {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.cannot_oper_filters), Snackbar.LENGTH_SHORT).show();
        }
    }

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
        getView().findViewById(R.id.coordinator)
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

    private void initSwipe(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                deleteSurprise(mAdapter, position);
            }
        }).attachToRecyclerView(recyclerView);
    }

}
