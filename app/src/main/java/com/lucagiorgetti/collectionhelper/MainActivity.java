package com.lucagiorgetti.collectionhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SurpRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static SearchView searchView;
    public static ArrayList<Surprise> surpriseList = new ArrayList<>();
    public static ArrayList<Surprise> allSurpriseList = new ArrayList<>();
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    public static ArrayList<Surprise> getAllSurpriseList() {
        return allSurpriseList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.insertData();
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_surp_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SurpRecyclerAdapter(this, surpriseList);
        mRecyclerView.setAdapter(mAdapter);

        searchView = (SearchView) findViewById(R.id.surprise_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        fireAuth = FirebaseAuth.getInstance();
        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    // User is logged out
                     Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    // Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Staring Login Activity
                    getApplicationContext().startActivity(i);
                }
            }
        };

        getMissingsFromServer();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(i);
                }
        });
    }

    private void getMissingsFromServer() {
        surpriseList.clear();
        allSurpriseList.clear();

        dbRef.child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        Surprise s = d.getValue(Surprise.class);
                        surpriseList.add(s);
                        allSurpriseList.add(s);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } if (id == R.id.action_logout){
            fireAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    
    @Override
    protected void onDestroy() {
        Log.w("MAIN","Destroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( fireAuthStateListener != null){
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireAuthStateListener);

    }

    private void insertData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users");
        DatabaseReference sets = database.getReference("sets");
        DatabaseReference surprises = database.getReference("surprises");
        DatabaseReference producers = database.getReference("producers");

        Producer kinder = new Producer("Kinder");
        producers.child("kinder").setValue(kinder);

        Set puffi = new Set("Puffi", 2016, kinder, "Sorpresa", "Italia", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/19145.jpg?alt=media&token=430ba17c-820e-4ebd-9577-4be99ec6bd78");
        sets.child("2016puffi").setValue(puffi);

        Set puffi2 = new Set("Puffi2", 2016, kinder, "Merendero", "Italia", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/19145.jpg?alt=media&token=430ba17c-820e-4ebd-9577-4be99ec6bd78");
        sets.child("2016puffi2").setValue(puffi2);

        Set puffi21 = new Set("Puffi21", 2016, kinder, "Culo", "Francia", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/19145.jpg?alt=media&token=430ba17c-820e-4ebd-9577-4be99ec6bd78");
        sets.child("2016puffi21").setValue(puffi21);

        Surprise SD324 = new Surprise("Puffetta", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/SD324.jpg?alt=media&token=2fa2a745-4079-4ae3-8482-4b7272fa207a","SD 324", puffi);
        surprises.child(SD324.getCode()).setValue(SD324);
    }
}
