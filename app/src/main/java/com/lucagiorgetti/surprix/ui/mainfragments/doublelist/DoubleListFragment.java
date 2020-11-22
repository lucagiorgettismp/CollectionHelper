package com.lucagiorgetti.surprix.ui.mainfragments.doublelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetDialogFragment;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetListener;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterPresenter;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterSelection;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao;

public class DoubleListFragment extends BaseFragment {

    private DoubleRecyclerAdapter mAdapter;
    private SearchView searchView;
    private View root;
    private View emptyList;
    private DoubleListDao doubleListDao;
    private FilterPresenter filterPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doubleListDao = new DoubleListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());

        DoubleListViewModel doubleListViewModel = new ViewModelProvider(this).get(DoubleListViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_double_list, container, false);
        }

        emptyList = root.findViewById(R.id.double_empty_list);
        ProgressBar progress = root.findViewById(R.id.double_loading);
        RecyclerView recyclerView = root.findViewById(R.id.double_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DoubleRecyclerAdapter();
        mAdapter.setListener(position -> deleteSurprise(mAdapter, position));
        recyclerView.setAdapter(mAdapter);

        doubleListViewModel.getDoubleSurprises().observe(getViewLifecycleOwner(), doubleList -> {
            emptyList.setVisibility(doubleList == null || doubleList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(doubleList);
            mAdapter.setFilterableList(doubleList);
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
                if (doubleList != null) {
                    filterPresenter = new FilterPresenter(doubleList);
                }
            } else {
                setTitle(getString(R.string.doubles));
            }
        });

        doubleListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        initSwipe(recyclerView);
        setHasOptionsMenu(true);
        setTitle(getString(R.string.doubles));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
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
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openBottomSheet();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBottomSheet() {
        if (filterPresenter != null){
            FilterSelection filterSelection = mAdapter.getFilterSelection();
            FilterBottomSheetDialogFragment bottomSheetDialogFragment = new FilterBottomSheetDialogFragment(filterPresenter, filterSelection);
            bottomSheetDialogFragment.setListener(new FilterBottomSheetListener() {
                @Override
                public void onFilterChanged(FilterSelection selection) {
                    mAdapter.setFilterSelection(selection);
                }

                @Override
                public void onFilterCleared() {
                    mAdapter.removeFilter();
                    bottomSheetDialogFragment.dismissAllowingStateLoss();
                }
            });
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "double_bottom_sheet");
        } else {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.cannot_oper_filters), Snackbar.LENGTH_SHORT).show();
        }
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


    private void deleteSurprise(DoubleRecyclerAdapter mAdapter, int position) {
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
                    .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> {
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
}

