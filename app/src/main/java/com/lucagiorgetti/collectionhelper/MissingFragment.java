package com.lucagiorgetti.collectionhelper;

import android.app.Fragment;
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
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class MissingFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    ArrayList<Surprise> missings = new ArrayList<>();
    private SurpRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.missings_fragment, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.missings_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SurpRecyclerAdapter(mContext, missings);
        recyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        getDataFromServer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        ArrayList<Surprise> filteredValues = new ArrayList<Surprise>(missings);
        for (Surprise value : missings) {
            if (!value.getDescription().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        mAdapter = new SurpRecyclerAdapter(mContext, filteredValues);
        recyclerView.setAdapter(mAdapter);
        return false;
    }

    public void resetSearch() {
        mAdapter = new SurpRecyclerAdapter(mContext, missings);
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

    private void getDataFromServer() {
        missings.clear();
        dbRef.child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        Surprise s = d.getValue(Surprise.class);
                        missings.add(s);
                        mAdapter = new SurpRecyclerAdapter(mContext, missings);
                        recyclerView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
