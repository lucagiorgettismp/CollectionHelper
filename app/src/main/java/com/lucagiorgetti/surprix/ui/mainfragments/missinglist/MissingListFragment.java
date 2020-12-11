package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetDialogFragment;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetListener;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

public class MissingListFragment extends BaseFragment {
    private MissingRecyclerAdapter mAdapter;
    private SearchView searchView;
    private View root;
    private View emptyList;
    private MissingListDao missingListDao;
    private MissingListViewModel missingListViewModel;
    private ChipFilters chipFilters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        missingListDao = new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());

        missingListViewModel = new ViewModelProvider(this).get(MissingListViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_missing_list, container, false);
        }
        emptyList = root.findViewById(R.id.missing_empty_list);
        ProgressBar progress = root.findViewById(R.id.missing_loading);
        RecyclerView recyclerView = root.findViewById(R.id.missing_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MissingRecyclerAdapter();

        mAdapter.setListener(new SurpRecylerAdapterListener() {
            @Override
            public void onShowMissingOwnerClick(Surprise surprise) {
                Navigation.findNavController(root).navigate(MissingListFragmentDirections.actionNavigationMissingListToNavigationMissingOwners(surprise.getId()));
            }

            @Override
            public void onSurpriseDelete(int position) {
                deleteSurprise(mAdapter, position);
            }
        });

        recyclerView.setAdapter(mAdapter);

        missingListViewModel.getMissingSurprises().observe(getViewLifecycleOwner(), missingList -> {
            emptyList.setVisibility(missingList == null || missingList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(missingList);
            mAdapter.setFilterableList(missingList);
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
                if (missingList != null) {
                    chipFilters = new ChipFilters(missingList);
                }
            } else {
                setTitle(getString(R.string.missings));
                //fab.setVisibility(View.GONE);
            }
        });

        missingListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        initSwipe(recyclerView);
        setHasOptionsMenu(true);
        setTitle(getString(R.string.missings));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openBottomSheet();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBottomSheet() {
        if (chipFilters != null) {
            FilterBottomSheetDialogFragment bottomSheetDialogFragment = new FilterBottomSheetDialogFragment(this.chipFilters);
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

    private void deleteSurprise(MissingRecyclerAdapter mAdapter, int position) {
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
                    .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> {
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
}