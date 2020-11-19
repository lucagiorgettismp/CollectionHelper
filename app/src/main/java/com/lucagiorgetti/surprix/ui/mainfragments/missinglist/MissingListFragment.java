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
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter.FilterBottomSheetDialogFragment;
import com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter.FilterBottomSheetListener;
import com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter.FilterPresenter;
import com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter.FilterSelection;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

import java.util.HashMap;
import java.util.List;

public class MissingListFragment extends BaseFragment {
    private MissingRecyclerAdapter mAdapter;
    private SearchView searchView;
    private View root;
    private View emptyList;
    private MissingListDao missingListDao;
    private MissingListViewModel missingListViewModel;
    private FilterPresenter filterPresenter;

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
                    filterPresenter = buildFilterPresenter(missingList);
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
        FilterBottomSheetDialogFragment bottomSheetDialogFragment = new FilterBottomSheetDialogFragment(filterPresenter, new FilterBottomSheetListener() {
            @Override
            public void onFilterChanged(FilterSelection selection) {
                mAdapter.setFilter(selection);
            }

            @Override
            public void onFilterCleared() {
                mAdapter.removeFilter();
            }
        });

        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "missing_bottom_sheet");
    }

    private FilterPresenter buildFilterPresenter(List<MissingSurprise> missingSurprises) {
        HashMap<String, String> categories = new HashMap<>();
        HashMap<String, String> producers = new HashMap<>();
        HashMap<String, String> years = new HashMap<>();

        for (MissingSurprise ms : missingSurprises) {
            Surprise surprise = ms.getSurprise();

            if (!categories.containsKey(surprise.getSet_category())) {
                categories.put(surprise.getSet_category(), surprise.getSet_category());
            }

            if (!producers.containsKey(surprise.getSet_producer_name())) {
                producers.put(surprise.getSet_producer_name(), surprise.getSet_producer_name());
            }

            if (!years.containsKey(surprise.getSet_year_name())) {
                years.put(surprise.getSet_year_name(), surprise.getSet_year_name());
            }
        }

        return new FilterPresenter(categories, producers, years);
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
        MissingSurprise missingSurprise = mAdapter.getItemAtPosition(position);
        CharSequence query = searchView.getQuery();
        missingListDao.removeMissing(missingSurprise.getSurprise().getId());
        mAdapter.removeFilterableItem(missingSurprise);
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
                        missingListDao.addMissing(missingSurprise.getSurprise().getId());
                        mAdapter.addFilterableItem(missingSurprise, position);
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