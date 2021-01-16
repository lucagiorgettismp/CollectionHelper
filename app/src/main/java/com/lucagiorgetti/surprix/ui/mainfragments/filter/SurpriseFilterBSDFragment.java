package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.ChipGroup;
import com.lucagiorgetti.surprix.R;

public class SurpriseFilterBSDFragment extends BottomSheetDialogFragment {
    private final ChipFilters chipFilters;

    private FilterBottomSheetListener listener;

    public SurpriseFilterBSDFragment(ChipFilters chipFilters) {
        this.chipFilters = chipFilters;
    }

    public void setListener(FilterBottomSheetListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_surprise, container,
                false);

        Button resetFilters = view.findViewById(R.id.filter_reset);
        resetFilters.setOnClickListener(v -> listener.onFilterCleared());

        ChipGroup categoriesChipsGroup = view.findViewById(R.id.filter_category_cg);
        ChipGroup producersChipsGroup = view.findViewById(R.id.filter_producer_cg);
        ChipGroup yearsChipsGroup = view.findViewById(R.id.filter_year_cg);

        for (FilterType type : FilterType.getValues(FilterSelectableType.SURPRISE)) {
            for (ChipFilter filter : chipFilters.getFiltersByType(type).values()) {
                BottomSheetChip chip = new BottomSheetChip(getContext(), filter);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    chipFilters.setFilterSelection(type, filter.getValue(), isChecked);
                    listener.onFilterChanged(chipFilters);
                });

                switch (type) {
                    case CATEGORY:
                        categoriesChipsGroup.addView(chip);
                        break;
                    case PRODUCER:
                        producersChipsGroup.addView(chip);
                        break;
                    case YEAR:
                        yearsChipsGroup.addView(chip);
                        break;
                }
            }
        }
        return view;
    }
}