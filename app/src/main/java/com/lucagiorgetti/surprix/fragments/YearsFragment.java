package com.lucagiorgetti.surprix.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.adapters.YearRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.views.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

public class YearsFragment extends Fragment{
    private FragmentListenerInterface listener;

    ArrayList<Year> years = new ArrayList<>();
    private YearRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private ProgressBar progress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.year_fragment, container, false);
        String producer_id = this.getArguments().getString("producer_id");
        progress = layout.findViewById(R.id.year_loading);
        recyclerView = layout.findViewById(R.id.year_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new YearRecyclerAdapter(mContext, years);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Year year = mAdapter.getItemAtPosition(position);
                listener.onYearClicked(year.getId(), year.getYear());
                SystemUtility.closeKeyboard(getActivity(), getView());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Year year = mAdapter.getItemAtPosition(position);
                listener.onLongYearClicked(year.getId(), year.getYear());
                SystemUtility.closeKeyboard(getActivity(), getView());
            }
        })
        );
        DatabaseUtility.getYearsFromProducer(producer_id, new OnGetListListener<Year>() {
            @Override
            public void onSuccess(ArrayList<Year> yearsList) {
                Collections.sort(yearsList, new Year.SortByDescYear());
                mAdapter = new YearRecyclerAdapter(mContext, yearsList);
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
                Toast.makeText(mContext, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
            }
        });

        showFirstTimeHelp();
        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentListenerInterface){
            this.listener = (FragmentListenerInterface) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        listener.setYearTitle();
        super.onResume();
    }

    private void showFirstTimeHelp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean show = prefs.getBoolean(SystemUtility.FIRST_TIME_YEAR_HELP_SHOW, true);
        if (show) {
            final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle(getString(R.string.smart_tip));
            alertDialog.setMessage(getString(R.string.tip_you_can_add_all_year));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_thanks),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                            edit.putBoolean(SystemUtility.FIRST_TIME_YEAR_HELP_SHOW, false);
                            edit.apply();
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
}


