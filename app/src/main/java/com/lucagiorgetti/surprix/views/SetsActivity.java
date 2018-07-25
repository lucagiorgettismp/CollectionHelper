package com.lucagiorgetti.surprix.views;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.model.Categories;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.utility.TitleHelper;

import java.util.ArrayList;
import java.util.Collections;


public class SetsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    ArrayList<Set> sets = new ArrayList<>();
    private SetRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle b = getIntent().getExtras();
        if (b != null){
            String yearId = b.getString("year_id");
            int yearNum = b.getInt("year_num");
            String prodName = b.getString("producer_name");

            progress = findViewById(R.id.search_set_loading);
            recyclerView = findViewById(R.id.set_search_recycler);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SetsActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new SetRecyclerAdapter(SetsActivity.this, sets);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(SetsActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Set set = mAdapter.getItemAtPosition(position);
                            onSetShortClick(set.getId(), set.getName());
                            SystemUtility.closeKeyboard(SetsActivity.this, getCurrentFocus());
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            Set set = mAdapter.getItemAtPosition(position);
                            onSetLongClick(set.getId(), set.getName());
                        }
                    })
            );
            DatabaseUtility.getSetsFromYear(yearId, new OnGetListListener<Set>() {
                @Override
                public void onSuccess(ArrayList<Set> setsList) {
                    if (setsList != null){
                        Collections.sort(setsList, new Set.SortBySetName());
                    }

                    ArrayList<Set> handpaintedSets = new ArrayList<>();
                    ArrayList<Set> compoSets = new ArrayList<>();

                    if (setsList != null) {
                        for (Set s : setsList){
                            if (s.getCategory().equals(Categories.HANDPAINTED)){
                                handpaintedSets.add(s);
                            } else {
                                compoSets.add(s);
                            }
                        }
                    }

                    handpaintedSets.addAll(compoSets);
                    sets = handpaintedSets;

                    mAdapter = new SetRecyclerAdapter(SetsActivity.this, sets);
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
                    Toast.makeText(SetsActivity.this, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
                }
            });

            showFirstTimeHelp();
            setTitle(prodName, yearNum);
        }
    }

    private void onSetLongClick(final String setId, String setName) {
        final AlertDialog alertDialog = new AlertDialog.Builder(SetsActivity.this).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + setName + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseUtility.addMissingsFromSet(setId);
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void onSetShortClick(String setId, String setName) {
        Bundle b = new Bundle();
        b.putString("set_id", setId);
        b.putString("set_name", setName);
        SystemUtility.openNewActivity(SetItemsActivity.class, b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search));

        return true;
    }

    private void showFirstTimeHelp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean show = prefs.getBoolean(SystemUtility.FIRST_TIME_SET_HELP_SHOW, true);
        if (show) {
            final AlertDialog alertDialog = new AlertDialog.Builder(SetsActivity.this).create();
            alertDialog.setTitle(getString(R.string.smart_tip));
            alertDialog.setMessage(getString(R.string.tip_you_can_add_all_set));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_thanks),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                            edit.putBoolean(SystemUtility.FIRST_TIME_SET_HELP_SHOW, false);
                            edit.apply();
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private void setTitle(String prodName, int yearNum) {
        TitleHelper.setSetSearchTitle(getSupportActionBar(), prodName, yearNum);
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
        mAdapter = new SetRecyclerAdapter(SetsActivity.this, filteredValues);
        recyclerView.setAdapter(mAdapter);
        return false;
    }

    public void resetSearch() {
        mAdapter = new SetRecyclerAdapter(SetsActivity.this, sets);
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
}
