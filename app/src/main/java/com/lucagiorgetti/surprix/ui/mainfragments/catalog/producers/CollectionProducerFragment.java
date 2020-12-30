package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.ui.activities.SettingsActivity;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;


public class CollectionProducerFragment extends BaseProducerFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_collection_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            SystemUtils.openNewActivity(SettingsActivity.class, null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupView() {
        ProducerViewModel producerViewModel = new ViewModelProvider(this).get(ProducerViewModel.class);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Producer producer = mAdapter.getItemAtPosition(position);
                        String prodName = producer.getName();

                        CollectionProducerFragmentDirections.ActionCollectionProducerFragmentToNavigationYear action = CollectionProducerFragmentDirections.actionCollectionProducerFragmentToNavigationYear(producer.getId(), prodName, CatalogNavigationMode.COLLECTION);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        producerViewModel.getProducers(CatalogNavigationMode.COLLECTION).observe(getViewLifecycleOwner(), producers -> {
            mAdapter.setYears(producers);
            mAdapter.notifyDataSetChanged();
        });

        producerViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        fab.setVisibility(View.GONE);
    }
}