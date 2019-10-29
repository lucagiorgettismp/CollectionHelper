package com.lucagiorgetti.surprix.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetDetailRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.utility.TitleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetDetailActivity extends AppCompatActivity {
    List<Surprise> surprises = new ArrayList<>();
    private SetDetailRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private String setId;
    private String setName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> onFabClicked());

        Bundle b = getIntent().getExtras();
        if (b != null) {
            setId = b.getString("set_id");
            setName = b.getString("set_name");

            recyclerView = findViewById(R.id.items_recycler);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SetDetailActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new SetDetailRecyclerAdapter();
            recyclerView.setAdapter(mAdapter);

            DatabaseUtility.getSurprisesBySet(setId, new FirebaseListCallback<Surprise>() {
                @Override
                public void onSuccess(List<Surprise> surprisesList) {
                    surprises = surprisesList;
                    mAdapter = new SetDetailRecyclerAdapter();
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

            showFirstTimeHelp();
            setTitle(setName);
        }
    }

    private void setTitle(String setName) {
        TitleHelper.setSetItemsTitle(getSupportActionBar(), setName);
    }

    private void showFirstTimeHelp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean show = prefs.getBoolean(SystemUtility.FIRST_TIME_SET_HELP_SHOW, true);
        if (show) {
            final AlertDialog alertDialog = new AlertDialog.Builder(SetDetailActivity.this).create();
            alertDialog.setTitle(getString(R.string.smart_tip));
            alertDialog.setMessage(getString(R.string.tip_you_can_add_all_set));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_thanks),
                    (dialog, which) -> {
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        edit.putBoolean(SystemUtility.FIRST_TIME_SET_HELP_SHOW, false);
                        edit.apply();
                        alertDialog.dismiss();
                    });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private void onFabClicked() {
        final AlertDialog alertDialog = new AlertDialog.Builder(SetDetailActivity.this).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + setName + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    DatabaseUtility.addMissingsFromSet(setId);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}
