package com.lucagiorgetti.surprix.ui.setlist;

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
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtility;

public class SetListFragment extends BaseFragment {

    private SetRecyclerAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SetListViewModel setListViewModel = ViewModelProviders.of(this).get(SetListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_set_list, container, false);

        ProgressBar progress = root.findViewById(R.id.set_loading);
        RecyclerView recyclerView = root.findViewById(R.id.set_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SetRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Set set = mAdapter.getItemAtPosition(position);
                        SetListFragmentDirections.SetSelectedAction action = SetListFragmentDirections.setSelectedAction(set.getId(), set.getName());
                        Navigation.findNavController(view).navigate(action);
                        SystemUtility.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        String yearId = null;
        if (getArguments() != null) {
            yearId = getArguments().getString("year_id");
            String yearName = getArguments().getString("year_name");
            setTitle(yearName);
        }

        setListViewModel.getSets(yearId).observe(this, sets -> {
            mAdapter.submitList(sets);
            mAdapter.setFilterableList(sets);
            mAdapter.notifyDataSetChanged();
        });

        setListViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}