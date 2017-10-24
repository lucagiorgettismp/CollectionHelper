package com.lucagiorgetti.collectionhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;
import com.lucagiorgetti.collectionhelper.model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ListView listView;
    public static ArrayList<Surprise> surpriseList = null;
    private static DbManager dbManager;
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.insertData();
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.surprise_list);
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

        dbManager = new DbManager(this);

        listView = (ListView) findViewById(R.id.surprise_list);

        if (fireAuth.getCurrentUser() != null) {
            this.surpriseList = dbManager.getMissings(fireAuth.getCurrentUser().getEmail());
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

            if (surpriseList != null) {
                final ArrayAdapter adapt = new SurpriseAdapter(MainActivity.this, R.layout.list_element, surpriseList, dbManager);

                SwipeDismissListViewTouchListener touchListener =
                        new SwipeDismissListViewTouchListener(listView,
                                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                    @Override
                                    public boolean canDismiss(int position) {
                                        return true;
                                    }

                                    @Override
                                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                        for (int position : reverseSortedPositions) {
                                            final Surprise itemClicked = (Surprise) listView.getItemAtPosition(position);
                                            new AlertDialog.Builder(MainActivity.this)
                                                    .setTitle("Rimozione")
                                                    .setMessage("Eliminare " + itemClicked.getCode() + " - " + itemClicked.getDescription() + "?")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            surpriseList.remove(itemClicked);
                                                            dbManager.deleteMissing(itemClicked);
                                                            adapt.notifyDataSetChanged();
                                                        }
                                                    })
                                                    .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    }
                                });
                listView.setOnTouchListener(touchListener);

                listView.setAdapter(adapt);
            }
        }
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

        Set puffi = new Set("Puffi", 2016, kinder, "Invernale", "Italia", "gs://collectionhelper.appspot.com/19145.jpg");
        sets.child("2016puffi").setValue(puffi);

        Set puffi2 = new Set("Puffi2", 2016, kinder, "Invernale", "Italia", "gs://collectionhelper.appspot.com/19145.jpg");
        sets.child("2016puffi2").setValue(puffi2);
    }
}
