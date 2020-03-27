package com.lucagiorgetti.surprix.ui.mainfragments.year;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class YearFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        YearViewModel yearViewModel = ViewModelProviders.of(this).get(YearViewModel.class);

        View root = inflater.inflate(R.layout.fragment_years, container, false);
        YearRecyclerAdapter mAdapter;
        ProgressBar progress = root.findViewById(R.id.year_loading);
        RecyclerView recyclerView = root.findViewById(R.id.year_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new YearRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Year year = mAdapter.getItemAtPosition(position);
                        YearFragmentDirections.YearSelectedAction action = YearFragmentDirections.yearSelectedAction(year.getId(), year.getDescr());
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

        String producerId = null;
        if (getArguments() != null) {
            producerId = getArguments().getString("producer_id");
            String producerName = getArguments().getString("producer_name");
            setTitle(producerName);
        }

        yearViewModel.getYears(producerId).observe(getViewLifecycleOwner(), years -> {
            mAdapter.setYears(years);
            mAdapter.notifyDataSetChanged();
        });

        yearViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        return root;
    }

    private void onLongYearClicked(final String yearId, int year) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_year_title));
        alertDialog.setMessage(getString(R.string.dialog_add_year_text) + " " + year + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    DatabaseUtils.addMissingsFromYear(yearId);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }


}