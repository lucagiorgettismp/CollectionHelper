package com.lucagiorgetti.surprix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.adapters.ThanksRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.model.Sponsor;

import java.util.ArrayList;
import java.util.Objects;

public class ThanksToFragment extends Fragment {
    private FragmentListenerInterface listener;

    ArrayList<Sponsor> sponsorsList = new ArrayList<>();
    private ThanksRecyclerAdapter mAdapter;
    private Context mContext;
    private RecyclerView recyclerView;
    private ProgressBar progress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.thanks_fragment, container, false);
        recyclerView = layout.findViewById(R.id.thanks_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        progress = layout.findViewById(R.id.thanks_loading);

        createSponsorList();
        mAdapter = new ThanksRecyclerAdapter(mContext, sponsorsList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Sponsor sponsor = mAdapter.getItemAtPosition(position);
                        if (sponsor.isClickable()) {
                            listener.onBannerClicked(sponsor.getUrl());
                        }
                        SystemUtility.closeKeyboard(Objects.requireNonNull(getActivity()));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return layout;
    }

    private void createSponsorList() {
        DatabaseUtility.getSponsors(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Sponsor s = d.getValue(Sponsor.class);
                        sponsorsList.add(s);
                        mAdapter = new ThanksRecyclerAdapter(mContext, sponsorsList);
                        recyclerView.setAdapter(mAdapter);
                        progress.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onStart() {
                sponsorsList.clear();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
                progress.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListenerInterface) {
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
        listener.setThanksToTitle();
        super.onResume();
    }

}


