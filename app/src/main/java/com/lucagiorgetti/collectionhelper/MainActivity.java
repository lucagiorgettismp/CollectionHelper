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
import com.lucagiorgetti.collectionhelper.Db.DbInitializer;
import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ListView listView;
    public static ArrayList<Surprise> surpriseList;
    private static DbManager dbManager;
    private static DbInitializer init;
    public static final String LOGGED = "logged";
    public static SessionManager session;
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        fireAuth = FirebaseAuth.getInstance();
        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // User is logged in
                    session.createLoginSession(user.getEmail());
                } else {
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

        //this.deleteDatabase("database.db");
        dbManager = new DbManager(this);
        //init = new DbInitializer(manager);
        //init.AddSurprises();

        setContentView(R.layout.activity_main);
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

        session.checkLogin();
        dbManager.getUserByUsername(session.getUserDetails().get("username"));
        listView = (ListView) findViewById(R.id.list);
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
        }
        return super.onOptionsItemSelected(item);
    }

    
    @Override
    protected void onDestroy() {
        Log.w("MAIN","Destroy");
        session.logoutUser();
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
        session.checkLogin();
        int userId = dbManager.getUserByUsername(session.getUserDetails().get("username")).getUserId();
        // surpriseList = manager.getSurprises();
        surpriseList = dbManager.getMissings(userId);

        final ArrayAdapter adapt = new SurpriseAdapter(this, R.layout.list_element, surpriseList, dbManager);
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
                                        .setMessage("Eliminare " + itemClicked.getCode() + " - " + itemClicked.getDesc() + "?")
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
