package com.lucagiorgetti.collectionhelper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.lucagiorgetti.collectionhelper.DatabaseUtility;
import com.lucagiorgetti.collectionhelper.FragmentListenerInterface;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.RecyclerItemClickListener;
import com.lucagiorgetti.collectionhelper.adapters.ProducerRecyclerAdapter;
import com.lucagiorgetti.collectionhelper.model.Producer;

import java.util.ArrayList;

public class ProducersFragment extends Fragment{
    private FragmentListenerInterface listener;

    ArrayList<Producer> producers = new ArrayList<>();
    private ProducerRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private ProgressBar progress;
    private static DatabaseReference dbRef = DatabaseUtility.getDatabase().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.producer_select_fragment, container, false);
        progress = (ProgressBar) layout.findViewById(R.id.prd_loading);
        recyclerView = (RecyclerView) layout.findViewById(R.id.prd_recycler);
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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
                Toast.makeText(mContext, "Errore nella sincronizzazione dei dati", Toast.LENGTH_SHORT).show();
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


