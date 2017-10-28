package com.lucagiorgetti.collectionhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.model.Set;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private SetRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static SearchView setsSearchView;
    public static ArrayList<Set> setsList = new ArrayList<>();
    public static ArrayList<Set> allSetsList = new ArrayList<>();
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_sets);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_sets_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SetRecyclerAdapter(this, setsList);
        mRecyclerView.setAdapter(mAdapter);

        setsSearchView = (SearchView) findViewById(R.id.set_search);
        setsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        getDataFromServer();
    }

    public void getDataFromServer(){
        setsList.clear();
        allSetsList.clear();

        dbRef.child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Set s = d.getValue(Set.class);
                        setsList.add(s);
                        allSetsList.add(s);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<Set> getAllSetsList() {
        return allSetsList;
    }
}
