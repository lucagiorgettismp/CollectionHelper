package com.lucagiorgetti.surprix.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.ProducerRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;

public class CatalogFragment extends Fragment {

    private CatalogViewModel catalogViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        catalogViewModel =
                ViewModelProviders.of(this).get(CatalogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        ProducerRecyclerAdapter mAdapter;
        ProgressBar progress = root.findViewById(R.id.catalog_loading);
        RecyclerView recyclerView = root.findViewById(R.id.catalog_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProducerRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Producer producer = mAdapter.getItemAtPosition(position);
                        String prodName = producer.getName();
                        if (producer.getProduct() != null) {
                            prodName = prodName + " " + producer.getProduct();
                        }

                        CatalogFragmentDirections.ProducerSelectedAction action = CatalogFragmentDirections.producerSelectedAction(producer.getId(), producer.getName());
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        catalogViewModel.getProducers().observe(this, producers -> {
            mAdapter.setYears(producers);
            mAdapter.notifyDataSetChanged();
        });

        catalogViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}