package com.lucagiorgetti.surprix.ui.mainfragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class SearchFragment extends BaseFragment {

    private SearchMode mode;
    private RecyclerView recyclerView;
    private SearchSurpriseRecyclerAdapter searchSurpriseRecyclerAdapter;
    private SearchSetRecyclerAdapter searchSetRecyclerAdapter;
    private SearchView searchView;
    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SearchViewModel searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_search, container, false);

        }
        RadioGroup radioGroup = root.findViewById(R.id.search_radio_group);
        recyclerView = root.findViewById(R.id.search_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        searchSurpriseRecyclerAdapter = new SearchSurpriseRecyclerAdapter();
        searchSetRecyclerAdapter = new SearchSetRecyclerAdapter();

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.search_radio_surprise:
                recyclerView.setAdapter(searchSurpriseRecyclerAdapter);
                mode = SearchMode.SURPRISE;
                break;
            case R.id.search_radio_sets:
                recyclerView.setAdapter(searchSetRecyclerAdapter);
                mode = SearchMode.SET;
                break;
        }

        radioGroup.setOnCheckedChangeListener((radioG, checkId) -> {
            switch (radioG.getCheckedRadioButtonId()) {
                case R.id.search_radio_surprise:
                    changeMode(SearchMode.SURPRISE);
                    break;
                case R.id.search_radio_sets:
                    changeMode(SearchMode.SET);
                    break;
            }
        });


        searchViewModel.getSurprises().observe(getViewLifecycleOwner(), surprises -> {
            searchSurpriseRecyclerAdapter.submitList(surprises);
            searchSurpriseRecyclerAdapter.setFilterableList(surprises);
        });

        searchViewModel.getSets().observe(getViewLifecycleOwner(), sets -> {
            searchSetRecyclerAdapter.submitList(sets);
            searchSetRecyclerAdapter.setFilterableList(sets);
        });

        searchView = root.findViewById(R.id.search_field);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchSetRecyclerAdapter.getFilter().filter(s);
                searchSurpriseRecyclerAdapter.getFilter().filter(s);
                return false;
            }
        });

        searchView.setQueryHint(getString(R.string.search));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String setId = null;
                        String setName = null;
                        switch (mode) {
                            case SURPRISE:
                                Surprise surprise = searchSurpriseRecyclerAdapter.getItemAtPosition(position);
                                setId = surprise.getSet_id();
                                setName = surprise.getSet_name();
                                break;
                            case SET:
                                Set set = searchSetRecyclerAdapter.getItemAtPosition(position);
                                setId = set.getId();
                                setName = set.getName();
                                break;
                        }
                        searchView.setQuery("", false);
                        SearchFragmentDirections.OnSearchedItemClick action = SearchFragmentDirections.onSearchedItemClick(setId, setName);
                        Navigation.findNavController(view).navigate(action);
                        SystemUtils.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        setTitle(getString(R.string.search_title));
        return root;
    }

    private void changeMode(SearchMode mode) {
        this.mode = mode;
        switch (mode) {
            case SURPRISE:
                recyclerView.setAdapter(searchSurpriseRecyclerAdapter);
                break;
            case SET:
                recyclerView.setAdapter(searchSetRecyclerAdapter);
                break;
        }
    }
}
