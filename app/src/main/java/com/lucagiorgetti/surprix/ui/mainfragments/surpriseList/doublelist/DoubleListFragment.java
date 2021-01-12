package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.doublelist;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseListFragment;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter;
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao;

public class DoubleListFragment extends BaseSurpriseListFragment {
    private DoubleListDao doubleListDao;
    private DoubleListViewModel doubleListViewModel;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.filter_search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.doubles));
            }
            return false;
        });
        searchView.setQueryHint(getString(R.string.search));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void deleteSurprise(SurpriseRecyclerAdapter mAdapter, int position) {
        Surprise surprise = mAdapter.getItemAtPosition(position);
        mAdapter.removeFilterableItem(surprise);

        CharSequence query = searchView.getQuery();
        if (query != null && query.length() != 0) {
            mAdapter.getFilter().filter(query);
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        doubleListDao.removeDouble(surprise.getId());

        if (mAdapter.getItemCount() > 0) {
            emptyList.setVisibility(View.GONE);
            setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
        } else {
            emptyList.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.doubles));
        }

        if (query != null && query.length() != 0) {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.double_removed), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.double_removed), Snackbar.LENGTH_LONG)
                    .setAction(SurprixApplication.getInstance().getString(R.string.discard_btn), view -> {
                        doubleListDao.addDouble(surprise.getId());
                        mAdapter.addFilterableItem(surprise, position);
                        mAdapter.notifyItemInserted(position);
                        if (mAdapter.getItemCount() > 0) {
                            setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
                            emptyList.setVisibility(View.GONE);
                        } else {
                            setTitle(getString(R.string.doubles));
                            emptyList.setVisibility(View.VISIBLE);
                        }
                    }).show();
        }
    }

    @Override
    public void setupData() {
        doubleListDao = new DoubleListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());

        mAdapter = new SurpriseRecyclerAdapter(SurpriseListType.DOUBLES);

        mAdapter.setListener(position -> deleteSurprise(mAdapter, position));

        doubleListViewModel = new ViewModelProvider(this).get(DoubleListViewModel.class);

        doubleListViewModel.getDoubleSurprises().observe(getViewLifecycleOwner(), doubleList -> {
            emptyList.setVisibility(doubleList == null || doubleList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(doubleList);
            mAdapter.setFilterableList(doubleList);
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
                if (doubleList != null) {
                    chipFilters = new ChipFilters();
                    chipFilters.initBySurprises(doubleList);
                }
            } else {
                setTitle(getString(R.string.doubles));
            }
        });

        doubleListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
                emptyList.setVisibility(View.GONE);
            } else {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setTitle(getString(R.string.doubles));
    }

    @Override
    public void loadData() {
        doubleListViewModel.loadDoubleSurprises();
    }
}

