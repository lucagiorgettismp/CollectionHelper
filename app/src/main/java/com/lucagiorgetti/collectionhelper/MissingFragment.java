package com.lucagiorgetti.collectionhelper;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;
import java.util.List;

public class MissingFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    ArrayList<Surprise> missings = new ArrayList<>();
    private SurpAdapter surpAdapter;
    private Context mContext;
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        getDataFromServer();

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
                            surpAdapter = new SurpAdapter(mContext, R.layout.missing_surprise_element, missings);
                            setListAdapter(surpAdapter);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }


    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        String item = (String) listView.getAdapter().getItem(position);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.search_fragment, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);
        TextView emptyTextView = (TextView) layout.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyTextView);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

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

        surpAdapter = new SurpAdapter(mContext, R.layout.missing_surprise_element, filteredValues);
        setListAdapter(surpAdapter);

        return false;
    }

    public void resetSearch() {
        surpAdapter = new SurpAdapter(mContext, R.layout.missing_surprise_element, missings);
        setListAdapter(surpAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

}
