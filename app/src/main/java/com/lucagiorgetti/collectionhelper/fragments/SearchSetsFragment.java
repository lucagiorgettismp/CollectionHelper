package com.lucagiorgetti.collectionhelper.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.lucagiorgetti.collectionhelper.DatabaseUtility;
import com.lucagiorgetti.collectionhelper.FragmentListenerInterface;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.RecyclerItemClickListener;
import com.lucagiorgetti.collectionhelper.adapters.SetRecyclerAdapter;
import com.lucagiorgetti.collectionhelper.model.Set;

import java.util.ArrayList;

public class SearchSetsFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private FragmentListenerInterface listener;

    private String yearId = null;
    ArrayList<Set> sets = new ArrayList<>();
    private SetRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private ProgressBar progress;
    private static DatabaseReference dbRef = DatabaseUtility.getDatabase().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.yearId = getArguments().getString("yearId");
        View layout = inflater.inflate(R.layout.set_search_fragment, container, false);
        progress = (ProgressBar) layout.findViewById(R.id.search_set_loading);
        recyclerView = (RecyclerView) layout.findViewById(R.id.set_search_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SetRecyclerAdapter(mContext, sets);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Set set = mAdapter.getItemAtPosition(position);
                listener.onSetShortClick(set.getId(), set.getName());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Set set = mAdapter.getItemAtPosition(position);
                listener.onSetLongClick(set.getId(), set.getName());
            }
        })
        );
        DatabaseUtility.getSetsFromYear(yearId, new OnGetListListener<Set>() {
            @Override
            public void onSuccess(ArrayList<Set> setsList) {
                sets = setsList;
                mAdapter = new SetRecyclerAdapter(mContext, sets);
                recyclerView.setAdapter(mAdapter);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onStart() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
                progress.setVisibility(View.GONE);
                Toast.makeText(mContext, "Errore nella sincronizzazione dei dati", Toast.LENGTH_SHORT).show();
            }
        });
        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Cerca");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }
        ArrayList<Set> filteredValues = new ArrayList<>(sets);
        for (Set value : sets) {
            if (!value.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        mAdapter = new SetRecyclerAdapter(mContext, filteredValues);
        recyclerView.setAdapter(mAdapter);
        return false;
    }

    public void resetSearch() {
        mAdapter = new SetRecyclerAdapter(mContext, sets);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentListenerInterface){
            this.listener = (FragmentListenerInterface) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        listener.setSearchTitle();
        super.onResume();
    }
}
