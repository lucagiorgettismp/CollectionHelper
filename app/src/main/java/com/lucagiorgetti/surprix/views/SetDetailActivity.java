package com.lucagiorgetti.surprix.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetDetailRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.TitleHelper;

import java.util.ArrayList;
import java.util.Objects;

public class SetDetailActivity extends AppCompatActivity {
    ArrayList<Surprise> surprises = new ArrayList<>();
    private SetDetailRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null){
            String setId = b.getString("set_id");
            String setName = b.getString("set_name");

            recyclerView = findViewById(R.id.items_recycler);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SetDetailActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new SetDetailRecyclerAdapter(SetDetailActivity.this, surprises);
            recyclerView.setAdapter(mAdapter);

            DatabaseUtility.getSurprisesBySet(setId, new OnGetListListener<Surprise>() {
                @Override
                public void onSuccess(ArrayList<Surprise> surprisesList) {
                    surprises = surprisesList;
                    mAdapter = new SetDetailRecyclerAdapter(SetDetailActivity.this, surprises);
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    Toast.makeText(SetDetailActivity.this, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
                }
            });

            setTitle(setName);
        }
    }

    private void setTitle(String setName) {
        TitleHelper.setSetItemsTitle(getSupportActionBar(), setName);
    }
}
