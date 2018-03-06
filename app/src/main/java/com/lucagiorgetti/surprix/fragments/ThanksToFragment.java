package com.lucagiorgetti.surprix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucagiorgetti.surprix.DatabaseUtility;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.SystemUtility;
import com.lucagiorgetti.surprix.adapters.ThanksRecyclerAdapter;
import com.lucagiorgetti.surprix.adapters.YearRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.model.Sponsor;
import com.lucagiorgetti.surprix.model.Year;

import java.util.ArrayList;

public class ThanksToFragment extends Fragment{
    private FragmentListenerInterface listener;

    ArrayList<Sponsor> sponsors = new ArrayList<>();
    private ThanksRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.thanks_fragment, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.thanks_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        createSponsorList();
        mAdapter = new ThanksRecyclerAdapter(mContext, sponsors);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Sponsor sponsor = mAdapter.getItemAtPosition(position);
                if(sponsor.isClickable()){
                    listener.onBannerClicked(sponsor.getUrl());
                }
                SystemUtility.closeKeyboard(getActivity(), getView());
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        })
        );

        return layout;
    }

    private void createSponsorList() {
        sponsors.add(new Sponsor("Banner1", getContext().getResources().getDrawable(R.drawable.banner_1), false, null));
        sponsors.add(new Sponsor("Banner2", getContext().getResources().getDrawable(R.drawable.banner_2), false, null));
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
        listener.setThanksToTitle();
        super.onResume();
    }

}


