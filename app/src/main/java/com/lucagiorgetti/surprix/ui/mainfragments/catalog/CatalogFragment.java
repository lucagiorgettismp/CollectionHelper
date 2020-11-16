package com.lucagiorgetti.surprix.ui.mainfragments.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;

public class CatalogFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CatalogViewModel catalogViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        CatalogRecyclerAdapter mAdapter;
        ProgressBar progress = root.findViewById(R.id.catalog_loading);

        FloatingActionButton fab = root.findViewById(R.id.catalog_fab_search);
        RecyclerView recyclerView = root.findViewById(R.id.catalog_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CatalogRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Producer producer = mAdapter.getItemAtPosition(position);
                        String prodName = producer.getName();
                        CatalogFragmentDirections.ProducerSelectedAction action = CatalogFragmentDirections.producerSelectedAction(producer.getId(), prodName);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        catalogViewModel.getProducers().observe(getViewLifecycleOwner(), producers -> {
            mAdapter.setYears(producers);
            mAdapter.notifyDataSetChanged();
        });

        catalogViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        fab.setOnClickListener(view -> Navigation.findNavController(view).navigate(CatalogFragmentDirections.goToSearch()));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}