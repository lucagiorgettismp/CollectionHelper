package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.missinglist;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseListFragment;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.MissingRecyclerAdapterListener;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter;
import com.lucagiorgetti.surprix.utility.dao.CollectionDao;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;
import com.lucagiorgetti.surprix.utility.dao.UserDao;

public class MissingListFragment extends BaseSurpriseListFragment {

    private MissingListDao missingListDao;
    private MissingListViewModel missingListViewModel;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
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
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnCloseListener(() -> {
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.missings));
            }
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void deleteSurprise(SurpriseRecyclerAdapter mAdapter, int position) {
        Surprise surprise = mAdapter.getItemAtPosition(position);
        CharSequence query = searchView.getQuery();
        missingListDao.removeMissing(surprise.getId());
        mAdapter.removeFilterableItem(surprise);
        if (query != null && query.length() != 0) {
            mAdapter.getFilter().filter(query);
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        if (mAdapter.getItemCount() > 0) {
            emptyList.setVisibility(View.GONE);
            setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
        } else {
            emptyList.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.missings));
        }

        if (query != null && query.length() != 0) {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.missing_removed), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.missing_removed), Snackbar.LENGTH_LONG)
                    .setAction(SurprixApplication.getInstance().getString(R.string.discard_btn), view -> {
                        missingListDao.addMissing(surprise.getId());
                        mAdapter.addFilterableItem(surprise, position);
                        mAdapter.notifyItemInserted(position);
                        if (mAdapter.getItemCount() > 0) {
                            emptyList.setVisibility(View.GONE);
                            setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
                        } else {
                            emptyList.setVisibility(View.VISIBLE);
                            setTitle(getString(R.string.missings));
                        }
                    }).show();
        }
    }

    @Override
    public void setupData() {

        missingListDao = new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());

        missingListViewModel = new ViewModelProvider(this).get(MissingListViewModel.class);

        mAdapter = new SurpriseRecyclerAdapter(SurpriseListType.MISSINGS);

        mAdapter.setListener(new MissingRecyclerAdapterListener() {
            @Override
            public void onShowMissingOwnerClick(Surprise surprise) {
                Navigation.findNavController(root).navigate(MissingListFragmentDirections.actionNavigationMissingListToNavigationMissingOwners(surprise.getId()));
            }

            @Override
            public void onSurpriseDelete(int position) {
                deleteSurprise(mAdapter, position);
            }
        });

        missingListViewModel.getMissingSurprises().observe(getViewLifecycleOwner(), missingList -> {
            emptyList.setVisibility(missingList == null || missingList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(missingList);
            mAdapter.setFilterableList(missingList);
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
                if (missingList != null) {
                    chipFilters = new ChipFilters();
                    chipFilters.initBySurprises(missingList);
                }
            } else {
                setTitle(getString(R.string.missings));
                //fab.setVisibility(View.GONE);
            }
        });

        missingListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                emptyList.setVisibility(View.GONE);
                showLoading();
            } else {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setTitle(getString(R.string.missings));
    }

    @Override
    public void loadData() {
        missingListViewModel.loadMissingSurprises();
    }
}