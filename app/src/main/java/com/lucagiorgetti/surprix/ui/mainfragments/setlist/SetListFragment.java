package com.lucagiorgetti.surprix.ui.mainfragments.setlist;

import android.app.AlertDialog;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

public class SetListFragment extends BaseFragment {

    private SetRecyclerAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SetListViewModel setListViewModel = new ViewModelProvider(this).get(SetListViewModel.class);

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
                        SystemUtils.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Set set = mAdapter.getItemAtPosition(position);
                        onLongSetClicked(set.getId(), set.getName());
                        SystemUtils.closeKeyboard(getActivity());
                    }
                })
        );

        String yearId = null;
        if (getArguments() != null) {
            yearId = getArguments().getString("year_id");
            String yearName = getArguments().getString("year_name");
            setTitle(yearName);
        }

        setListViewModel.getSets(yearId).observe(getViewLifecycleOwner(), sets -> {
            mAdapter.submitList(sets);
            mAdapter.setFilterableList(sets);
        });

        setListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        setHasOptionsMenu(true);

        return root;
    }

    private void onLongSetClicked(String setId, String setName) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + setName + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).addMissingsBySet(setId);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
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

        searchView.setQueryHint(SurprixApplication.getInstance().getString(R.string.search));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}