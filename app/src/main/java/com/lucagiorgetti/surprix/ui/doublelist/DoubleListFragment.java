package com.lucagiorgetti.surprix.ui.doublelist;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.ui.adapters.SurpriseRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

public class DoubleListFragment extends BaseFragment {

    private SurpriseRecyclerAdapter mAdapter;
    private SearchView searchView;
    private View root;
    private DoubleListViewModel doubleListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doubleListViewModel = ViewModelProviders.of(this).get(DoubleListViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_double_list, container, false);
        }

        View emptyList = root.findViewById(R.id.double_empty_list);
        ProgressBar progress = root.findViewById(R.id.double_loading);
        RecyclerView recyclerView = root.findViewById(R.id.double_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SurpriseRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        doubleListViewModel.getDoubleSurprises().observe(this, doubleList -> {
            emptyList.setVisibility(doubleList == null || doubleList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(doubleList);
            mAdapter.setFilterableList(doubleList);
            if (mAdapter.getItemCount() > 0){
                setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.doubles));
            }
        });

        doubleListViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

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

        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSwipe( RecyclerView recyclerView) {
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

    private void deleteSurprise(SurpriseRecyclerAdapter mAdapter, int position) {
        Surprise surprise = mAdapter.getItemAtPosition(position);
        mAdapter.removeFilterableItem(surprise);

        CharSequence query = searchView.getQuery();
        if (query != null && query.length() != 0) {
            mAdapter.getFilter().filter(query);
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        DatabaseUtility.removeDouble(surprise.getId());

        if (mAdapter.getItemCount() > 0){
            setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
        } else {
            setTitle(getString(R.string.doubles));
        }

        Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.double_removed), Snackbar.LENGTH_LONG)
                .setAction(SurprixApplication.getInstance().getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseUtility.addMissing(surprise.getId());
                        doubleListViewModel.addDouble(surprise, position);
                        mAdapter.notifyItemInserted(position);
                        if (mAdapter.getItemCount() > 0){
                            setTitle(getString(R.string.doubles) + " (" + mAdapter.getItemCount() + ")");
                        } else {
                            setTitle(getString(R.string.doubles));
                        }
                    }
                }).show();
    }
}

