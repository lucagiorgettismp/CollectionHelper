package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.lucagiorgetti.surprix.R;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private final ChipFilters chipFilters;

    private FilterBottomSheetListener listener;

    public FilterBottomSheetDialogFragment(ChipFilters chipFilters) {
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

        View view = inflater.inflate(R.layout.bottom_sheet, container,
                false);

        Button resetFilters = view.findViewById(R.id.filter_reset);
        resetFilters.setOnClickListener(v -> listener.onFilterCleared());

        ChipGroup categoriesChipsGroup = view.findViewById(R.id.filter_category_cg);
        ChipGroup producersChipsGroup = view.findViewById(R.id.filter_producer_cg);
        ChipGroup yearsChipsGroup = view.findViewById(R.id.filter_year_cg);

        for (FilterType type : FilterType.values()) {
            for (ChipFilter asd : chipFilters.getFiltersByType(type).values()) {
                BottomSheetChip chip = new BottomSheetChip(getContext(), asd);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    chipFilters.setFilterSelection(type, asd.getValue(), isChecked);
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

class BottomSheetChip extends Chip {
    public BottomSheetChip(Context context, ChipFilter chipFilter) {
        super(context);
        this.setText(chipFilter.getName());
        this.setCloseIconVisible(true);
        this.setCheckedIconVisible(true);
        this.setCloseIconVisible(false);
        this.setCheckable(true);
        this.setChecked(chipFilter.isSelected());
    }

    public BottomSheetChip(Context context) {
        super(context);
    }

    public BottomSheetChip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}