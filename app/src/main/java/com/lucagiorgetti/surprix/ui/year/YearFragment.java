package com.lucagiorgetti.surprix.ui.year;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.YearRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtility;

public class YearFragment extends BaseFragment {

    private YearViewModel yearViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yearViewModel =
                ViewModelProviders.of(this).get(YearViewModel.class);

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
                        SystemUtility.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Year year = mAdapter.getItemAtPosition(position);
                        onLongYearClicked(year.getId(), year.getYear());
                        SystemUtility.closeKeyboard(getActivity());
                    }
                })
        );

        String producerId = null;
        if (getArguments() != null) {
            producerId = getArguments().getString("producer_id");
            String producerName = getArguments().getString("producer_name");
            setTitle(producerName);
        }

        yearViewModel.getYears(producerId).observe(this, years -> {
            mAdapter.setYears(years);
            mAdapter.notifyDataSetChanged();
        });

        yearViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onLongYearClicked(final String yearId, int year) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.dialog_add_year_title));
        alertDialog.setMessage(getString(R.string.dialog_add_year_text) + " " + year + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    DatabaseUtility.addMissingsFromYear(yearId);
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }


}