package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
    CatalogNavigationMode navigationMode;
    String yearId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            yearId = SetListFragmentArgs.fromBundle(getArguments()).getYearId();
            String yearName = SetListFragmentArgs.fromBundle(getArguments()).getYearName();
            navigationMode = SetListFragmentArgs.fromBundle(getArguments()).getNavigationMode();
            setTitle(yearName);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupView() {
        SetListViewModel setListViewModel = new ViewModelProvider(this).get(SetListViewModel.class);

        /*
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Set set = mAdapter.getItemAtPosition(position);
                        SetListFragmentDirections.SetSelectedAction action = SetListFragmentDirections.setSelectedAction(set.getId(), set.getName(), navigationMode);
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
        */

        setListViewModel.getSets(yearId, navigationMode).observe(getViewLifecycleOwner(), sets -> {
            mAdapter.submitList(sets);
            mAdapter.setFilterableList(sets);
        });

        setListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
    }

    @Override
    public SetRecyclerAdapter setAdapter() {
        return new SetRecyclerAdapter(navigationMode, new MyClickListener() {
            @Override
            public void onSetInCollectionChanged(Set set, boolean isChecked) {
                Toast.makeText(getContext(), "Switch per set: " + set.getId() + " mode: " + isChecked, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSetClicked(Set set) {
                SetListFragmentDirections.SetSelectedAction action = SetListFragmentDirections.setSelectedAction(set.getId(), set.getName(), navigationMode);
                Navigation.findNavController(getView()).navigate(action);
                SystemUtils.closeKeyboard(getActivity());
            }
        });
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

    public interface MyClickListener {
        void onSetInCollectionChanged(Set set, boolean isChecked);

        void onSetClicked(Set set);
    }

}