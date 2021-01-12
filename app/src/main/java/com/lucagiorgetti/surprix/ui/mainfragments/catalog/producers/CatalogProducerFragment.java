package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;

public class CatalogProducerFragment extends BaseProducerFragment {
    @Override
    public void setupView() {
        ProducerViewModel producerViewModel = new ViewModelProvider(this).get(ProducerViewModel.class);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Producer producer = mAdapter.getItemAtPosition(position);
                        String prodName = producer.getName();
                        CatalogProducerFragmentDirections.ProducerSelectedAction action = CatalogProducerFragmentDirections.producerSelectedAction(producer.getId(), prodName, CatalogNavigationMode.CATALOG);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        producerViewModel.getProducers(CatalogNavigationMode.CATALOG).observe(getViewLifecycleOwner(), producers -> {
            mAdapter.setYears(producers);
            mAdapter.notifyDataSetChanged();
        });

        producerViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        fab.setOnClickListener(view -> Navigation.findNavController(view).navigate(CatalogProducerFragmentDirections.goToSearch()));
    }
}