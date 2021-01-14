package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.CollectionSetFilterBSDFragment;
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.lucagiorgetti.surprix.utility.dao.CollectionDao;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

public class SetListFragment extends BaseSetListFragment {
    CatalogNavigationMode navigationMode;
    String yearId;
    String producerId;
    CollectionDao collectionDao;
    MissingListDao missingListDao;
    SetListViewModel setListViewModel;
    private ChipFilters chipFilters;

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

            if (navigationMode.equals(CatalogNavigationMode.CATALOG) && !SystemUtils.isSetHintDisplayed()) {
                showHintAlert();
            }
        }

        super.onCreate(savedInstanceState);
    }

    private void showHintAlert() {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Hint")
                .setMessage("To easily create your missing list, add the set to your collection and keep the set pressed. You will add all of its surprises to your missing list!")
                .setPositiveButton("Ok. thanks", (dialog1, which) -> {
                    SystemUtils.setSetHintDisplayed(true);
                    dialog1.dismiss();
                })
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void setupView() {
        setListViewModel = new ViewModelProvider(this).get(SetListViewModel.class);
        setListViewModel.getSets(yearId, producerId, navigationMode).observe(getViewLifecycleOwner(), sets -> {
            mAdapter.submitList(sets);
            mAdapter.setFilterableList(sets);

            if (navigationMode == CatalogNavigationMode.COLLECTION) {
                chipFilters = new ChipFilters();
                chipFilters.initByCatalogSets(sets);
            }
        });

        setListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        switch (navigationMode) {
            case CATALOG:
                inflater.inflate(R.menu.search_menu, menu);
                break;
            case COLLECTION:
                inflater.inflate(R.menu.filter_search_menu, menu);
                break;
        }

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnCloseListener(() -> {
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.missings));
            }
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openBottomSheet();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBottomSheet() {
        if (chipFilters != null) {
            CollectionSetFilterBSDFragment bottomSheetDialogFragment = new CollectionSetFilterBSDFragment(this.chipFilters);
            bottomSheetDialogFragment.setListener(new FilterBottomSheetListener() {
                @Override
                public void onFilterChanged(ChipFilters selection) {
                    mAdapter.setChipFilters(selection);
                }

                @Override
                public void onFilterCleared() {
                    chipFilters.clearSelection();
                    mAdapter.setChipFilters(chipFilters);
                    bottomSheetDialogFragment.dismissAllowingStateLoss();
                }
            });
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "double_bottom_sheet");
        } else {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.cannot_oper_filters), Snackbar.LENGTH_SHORT).show();
        }
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
            public boolean onSetLongClicked(Set set, boolean inCollection) {
                onLongSetClicked(set, inCollection);
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

    private void onLongSetClicked(Set set, boolean inCollection) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + set.getName() + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).addMissingsBySet(set.getId());

                    if (!inCollection) {
                        setListViewModel.loadSets(yearId, producerId, navigationMode);
                    }
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    public interface MyClickListener {
        void onSetInCollectionChanged(Set set, boolean isChecked);

        void onSetClicked(Set set);

        boolean onSetLongClicked(Set set, boolean inCollection);
    }

}