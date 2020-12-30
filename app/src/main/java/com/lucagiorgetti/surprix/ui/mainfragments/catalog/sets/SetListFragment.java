package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.AlertDialog;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

public class SetListFragment extends BaseSetListFragment {

    @Override
    public void setupView() {
        SetListViewModel setListViewModel = new ViewModelProvider(this).get(SetListViewModel.class);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Set set = mAdapter.getItemAtPosition(position);
                        SetListFragmentDirections.SetSelectedAction action = SetListFragmentDirections.setSelectedAction(set.getId(), set.getName(), CatalogNavigationMode.CATALOG);
                        Navigation.findNavController(view).navigate(action);
                        SystemUtils.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Set set = mAdapter.getItemAtPosition(position);
                        onLongSetClicked(set.getId(), set.getName());
                        SystemUtils.closeKeyboard(getActivity());
                    }
                })
        );

        String yearId = null;
        if (getArguments() != null) {
            yearId = getArguments().getString("year_id");
            String yearName = getArguments().getString("year_name");
            setTitle(yearName);
        }

        setListViewModel.getSets(yearId, CatalogNavigationMode.CATALOG).observe(getViewLifecycleOwner(), sets -> {
            mAdapter.submitList(sets);
            mAdapter.setFilterableList(sets);
        });

        setListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
    }

    private void onLongSetClicked(String setId, String setName) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + setName + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).addMissingsBySet(setId);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }


}