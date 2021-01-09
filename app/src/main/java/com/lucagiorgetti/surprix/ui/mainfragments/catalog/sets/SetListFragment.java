package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.lucagiorgetti.surprix.utility.dao.CollectionDao;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

import java.util.ArrayList;

public class SetListFragment extends BaseSetListFragment {
    CatalogNavigationMode navigationMode;
    String yearId;
    String producerId;
    CollectionDao collectionDao;
    MissingListDao missingListDao;
    SetListViewModel setListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String username = SurprixApplication.getInstance().getCurrentUser().getUsername();
        collectionDao = new CollectionDao(username);
        missingListDao = new MissingListDao(username);

        if (getArguments() != null) {
            yearId = SetListFragmentArgs.fromBundle(getArguments()).getYearId();
            producerId = SetListFragmentArgs.fromBundle(getArguments()).getProducerId();
            String yearName = SetListFragmentArgs.fromBundle(getArguments()).getYearName();
            navigationMode = SetListFragmentArgs.fromBundle(getArguments()).getNavigationMode();
            setTitle(yearName);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupView() {
        setListViewModel = new ViewModelProvider(this).get(SetListViewModel.class);
        setListViewModel.getSets(yearId, producerId, navigationMode).observe(getViewLifecycleOwner(), sets -> {
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
                if (isChecked) {
                    collectionDao.addSetInCollection(set);
                } else {
                    alertRemoveCollection(set);
                }
            }

            @Override
            public void onSetClicked(Set set) {
                SetListFragmentDirections.SetSelectedAction action = SetListFragmentDirections.setSelectedAction(set.getId(), set.getName(), navigationMode);
                Navigation.findNavController(getView()).navigate(action);
                SystemUtils.closeKeyboard(getActivity());
            }

            @Override
            public boolean onSetLongClicked(Set set) {
                onLongSetClicked(set);
                return true;
            }
        });
    }

    private void alertRemoveCollection(Set set) {
        final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.remove_from_collection_title));
        alertDialog.setMessage(getString(R.string.remove_from_collection_message));
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.proceed_btn),
                (dialog, which) -> {
                    missingListDao.removeItemsFromSet(set);
                    collectionDao.removeSetInCollection(set);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn),
                (dialog, which) -> {
                    alertDialog.dismiss();
                    setListViewModel.getSets(yearId, producerId, navigationMode);
                    mAdapter.notifyDataSetChanged();
                });
        alertDialog.show();
    }

    private void onLongSetClicked(Set set) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + set.getName() + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).addMissingsBySet(set.getId());
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    public interface MyClickListener {
        void onSetInCollectionChanged(Set set, boolean isChecked);

        void onSetClicked(Set set);

        boolean onSetLongClicked(Set set);
    }

}