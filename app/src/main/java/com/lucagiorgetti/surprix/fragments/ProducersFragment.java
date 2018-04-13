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
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.adapters.ProducerRecyclerAdapter;

import java.util.ArrayList;

public class ProducersFragment extends Fragment{
    private FragmentListenerInterface listener;

    ArrayList<Producer> producers = new ArrayList<>();
    private ProducerRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private ProgressBar progress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.producer_fragment, container, false);
        progress = layout.findViewById(R.id.prd_loading);
        recyclerView = layout.findViewById(R.id.prd_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProducerRecyclerAdapter(mContext, producers);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Producer producer = mAdapter.getItemAtPosition(position);
                String prod_name = producer.getName();
                if(producer.getProduct() != null){
                    prod_name = prod_name + " " + producer.getProduct();
                }
                listener.onProducerClick(producer.getId(), prod_name);
                SystemUtility.closeKeyboard(getActivity(), getView());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        })
        );
        DatabaseUtility.getProducers(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        Producer p = d.getValue(Producer.class);
                        producers.add(p);
                        mAdapter = new ProducerRecyclerAdapter(mContext, producers);
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
                Toast.makeText(mContext, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
            }
        });
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
        listener.setProducerTitle();
        super.onResume();
    }

}


