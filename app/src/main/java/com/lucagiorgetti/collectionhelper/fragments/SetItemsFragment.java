package com.lucagiorgetti.collectionhelper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucagiorgetti.collectionhelper.DatabaseUtility;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.adapters.SetItemAdapter;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class SetItemsFragment extends Fragment implements View.OnClickListener{
    private FragmentListenerInterface listener;

    ArrayList<Surprise> surprises = new ArrayList<>();
    private SetItemAdapter mAdapter;
    private GridView gridView;
    private Context mContext;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.set_items_fragment, container, false);

        String setClicked = getArguments().getString("set");
        progress = (ProgressBar) layout.findViewById(R.id.items_loading);

        mAdapter = new SetItemAdapter(mContext, surprises);
        gridView = (GridView) layout.findViewById(R.id.items_gridview);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.btn_item_add_missing){
                    listener.onItemAddMissings(surprises.get(position).getId());
                    Snackbar.make(layout, getString(R.string.added_to_missings)+ ": " + surprises.get(position).getDescription() , Snackbar.LENGTH_SHORT).show();
                } else if (viewId == R.id.btn_item_add_double){
                    listener.onItemAddDoubles(surprises.get(position).getId());
                    Snackbar.make(layout, getString(R.string.added_to_doubles) + ": " + surprises.get(position).getDescription() , Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        DatabaseUtility.getSurprisesBySet(setClicked, new OnGetListListener<Surprise>() {
            @Override
            public void onSuccess(ArrayList<Surprise> surprisesList) {
                surprises = surprisesList;
                mAdapter = new SetItemAdapter(mContext, surprises);
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
        listener.setItemsTitle();
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}
