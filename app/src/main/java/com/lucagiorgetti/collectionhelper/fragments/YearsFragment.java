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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.DatabaseUtility;
import com.lucagiorgetti.collectionhelper.FragmentListenerInterface;
import com.lucagiorgetti.collectionhelper.OnGetDataListener;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.RecyclerItemClickListener;
import com.lucagiorgetti.collectionhelper.adapters.ProducerRecyclerAdapter;
import com.lucagiorgetti.collectionhelper.adapters.YearRecyclerAdapter;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.Year;

import java.util.ArrayList;

public class YearsFragment extends Fragment{
    private FragmentListenerInterface listener;

    ArrayList<Year> years = new ArrayList<>();
    private YearRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private ProgressBar progress;
    private String producer_id = null;
    private static DatabaseReference dbRef = DatabaseUtility.getDatabase().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.year_select_fragment, container, false);
        this.producer_id = this.getArguments().getString("producer_id");
        progress = (ProgressBar) layout.findViewById(R.id.year_loading);
        recyclerView = (RecyclerView) layout.findViewById(R.id.year_recycler);
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Year year = mAdapter.getItemAtPosition(position);
                //listener.onLongYearClicked(year.getId(), year.getYear());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        })
        );
        getDataFromServer(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onSuccess() {
                mAdapter = new YearRecyclerAdapter(mContext, years);
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

    private void getDataFromServer(final OnGetDataListener listen) {
        listen.onStart();
        years.clear();

        dbRef.child("producers").child(this.producer_id).child("years").orderByChild("year").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        dbRef.child("years").child(d.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Year y = snapshot.getValue(Year.class);
                                    years.add(y);
                                }
                                listen.onSuccess();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                listen.onFailure();
                            }
                        });
                    }
                } else {
                    listen.onSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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

}


