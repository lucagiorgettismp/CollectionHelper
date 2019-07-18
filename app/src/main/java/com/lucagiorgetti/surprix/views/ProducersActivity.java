package com.lucagiorgetti.surprix.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.ProducerRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.utility.TitleHelper;

import java.util.ArrayList;

public class ProducersActivity extends AppCompatActivity {
    ArrayList<Producer> producers = new ArrayList<>();
    private ProducerRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_fragment);

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

        progress = findViewById(R.id.prd_loading);
        recyclerView = findViewById(R.id.prd_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProducersActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProducerRecyclerAdapter(ProducersActivity.this, producers);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ProducersActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Producer producer = mAdapter.getItemAtPosition(position);
                        String prod_name = producer.getName();
                        if (producer.getProduct() != null) {
                            prod_name = prod_name + " " + producer.getProduct();
                        }
                        onProducerClick(producer.getId(), prod_name);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        DatabaseUtility.getProducers(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Producer p = d.getValue(Producer.class);
                        producers.add(p);
                        mAdapter = new ProducerRecyclerAdapter(ProducersActivity.this, producers);
                        recyclerView.setAdapter(mAdapter);
                        progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onStart() {
                producers.clear();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
                progress.setVisibility(View.GONE);
                Toast.makeText(ProducersActivity.this, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
            }
        });

        setTitle();
    }

    private void setTitle() {
        TitleHelper.setProducerTitle(getSupportActionBar());
    }

    private void onProducerClick(String id, String prod_name) {
        Bundle b = new Bundle();
        b.putString("producer_name", prod_name);
        b.putString("producer_id", id);
        SystemUtility.openNewActivity(YearsActivity.class, b);
    }
}
