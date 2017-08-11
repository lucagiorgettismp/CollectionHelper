package com.lucagiorgetti.collectionhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lucagiorgetti.collectionhelper.Db.DbInitializer;
import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ListView listView;
    public static ArrayList<Surprise> surpriseList;
    private static DbManager manager;
    private static DbInitializer init;
    public static final String LOGGED = "logged";
    public static String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        if(getUserLogged() == null){
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        } else {
            username = getUserLogged();
        }

        listView = (ListView) findViewById(R.id.list);
        manager = new DbManager(this);
        init = new DbInitializer(manager);

        //this.deleteDatabase("database.db");

        surpriseList = manager.getSurprises();

        //Aggiunge elementi in db
        if(surpriseList.isEmpty()){
            init.AddSurprises();
        }

        final ArrayAdapter adapt = new SurpriseAdapter(this, R.layout.list_element, surpriseList, manager);
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
                                            .setMessage("Eliminare "+ itemClicked.getCode() + " - " + itemClicked.getDesc() + "?")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    surpriseList.remove(itemClicked);
                                                    manager.deleteSurprise(itemClicked);
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

    public String getUserLogged() {
        SharedPreferences prefs = getSharedPreferences(LOGGED, MODE_PRIVATE);
        String login = prefs.getString("username", null);
        String username = null;
        if (login != null) {
            username = prefs.getString("name", "Guest");
        }
        return username;
    }
    public void unlogUser(){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LOGGED, MODE_PRIVATE).edit();
        editor.remove("username");
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        unlogUser();
        super.onDestroy();
    }
}
