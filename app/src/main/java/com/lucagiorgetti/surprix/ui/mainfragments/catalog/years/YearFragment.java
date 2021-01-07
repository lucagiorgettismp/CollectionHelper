package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

public class YearFragment extends BaseYearFragment {
    CatalogNavigationMode navigationMode;

    @Override
    public void setupView() {
        YearViewModel yearViewModel = new ViewModelProvider(this).get(YearViewModel.class);

        String producerId = null;
        if (getArguments() != null) {
            producerId = YearFragmentArgs.fromBundle(getArguments()).getProducerId();
            String producerName = YearFragmentArgs.fromBundle(getArguments()).getProducerName();
            navigationMode = YearFragmentArgs.fromBundle(getArguments()).getNavigationMode();
            setTitle(producerName);
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Year year = mAdapter.getItemAtPosition(position);
                        YearFragmentDirections.YearSelectedAction action = YearFragmentDirections.yearSelectedAction(year.getId(), year.getDescr(), navigationMode);
                        Navigation.findNavController(view).navigate(action);
                        SystemUtils.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Year year = mAdapter.getItemAtPosition(position);
                        onLongYearClicked(year.getId(), year.getYear());
                        SystemUtils.closeKeyboard(getActivity());
                    }
                })
        );

        yearViewModel.getYears(producerId, navigationMode).observe(getViewLifecycleOwner(), years -> {
            mAdapter.setYears(years);
            mAdapter.notifyDataSetChanged();
        });

        yearViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
    }

    private void onLongYearClicked(final String yearId, int year) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_year_title));
        alertDialog.setMessage(getString(R.string.dialog_add_year_text) + " " + year + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).addMissingsByYear(yearId);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }


}