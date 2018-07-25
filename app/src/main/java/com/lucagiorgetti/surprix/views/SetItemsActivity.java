package com.lucagiorgetti.surprix.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetItemAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.TitleHelper;

import java.util.ArrayList;

public class SetItemsActivity extends AppCompatActivity {
    ArrayList<Surprise> surprises = new ArrayList<>();
    private SetItemAdapter mAdapter;
    private GridView gridView;
    private ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final View parentLayout = findViewById(android.R.id.content);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_detail_fragment);

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
            String setId = b.getString("set_id");
            String setName = b.getString("set_name");
            progress = findViewById(R.id.items_loading);

            mAdapter = new SetItemAdapter(SetItemsActivity.this, surprises);
            gridView = findViewById(R.id.items_gridview);
            gridView.setAdapter(mAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long viewId = view.getId();
                    if (viewId == R.id.btn_item_add_missing){
                        onItemAddMissings(surprises.get(position).getId());
                        Snackbar.make(parentLayout, getString(R.string.added_to_missings)+ ": " + surprises.get(position).getDescription() , Snackbar.LENGTH_SHORT).show();
                    } else if (viewId == R.id.btn_item_add_double){
                        onItemAddDoubles(surprises.get(position).getId());
                        Snackbar.make(parentLayout, getString(R.string.added_to_doubles) + ": " + surprises.get(position).getDescription() , Snackbar.LENGTH_SHORT).show();
                    }
                }
            });


            DatabaseUtility.getSurprisesBySet(setId, new OnGetListListener<Surprise>() {
                @Override
                public void onSuccess(ArrayList<Surprise> surprisesList) {
                    surprises = surprisesList;
                    mAdapter = new SetItemAdapter(SetItemsActivity.this, surprises);
                    gridView.setAdapter(mAdapter);
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onStart() {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure() {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(SetItemsActivity.this, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
                }
            });

            setTitle(setName);
        }
    }

    private void onItemAddMissings(String surpId) {
        DatabaseUtility.addMissing(surpId);
    }

    private void onItemAddDoubles(String surpId) {
        DatabaseUtility.addDouble(surpId);
    }


    private void setTitle(String setName) {
        TitleHelper.setSetItemsTitle(getSupportActionBar(), setName);
    }
}
