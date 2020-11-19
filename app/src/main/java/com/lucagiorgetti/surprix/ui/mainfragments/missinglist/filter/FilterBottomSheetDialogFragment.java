package com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Categories;

import java.util.Map;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {
    ChipGroup yearsChipsGroup;
    ChipGroup producersChipsGroup;
    ChipGroup categoriesChipsGroup;

    FilterPresenter filterPresenter;
    FilterBottomSheetListener listener;

    public FilterBottomSheetDialogFragment(FilterPresenter filterPresenter, FilterBottomSheetListener listener) {
        this.filterPresenter = filterPresenter;
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

        categoriesChipsGroup = view.findViewById(R.id.filter_category_cg);
        producersChipsGroup = view.findViewById(R.id.filter_producer_cg);
        yearsChipsGroup = view.findViewById(R.id.filter_year_cg);

        FilterSelection filterSelection = new FilterSelection();
        for (Map.Entry<String, String> pair : filterPresenter.getYears().entrySet()) {
            FilterChip chip = new FilterChip(getContext(), pair.getKey());
            filterSelection.addYear(pair.getKey());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    filterSelection.addYear(pair.getValue());
                } else {
                    filterSelection.removeYear(pair.getValue());
                }
                listener.onFilterChanged(filterSelection);
            });
            yearsChipsGroup.addView(chip);
        }

        for (Map.Entry<String, String> pair : filterPresenter.getProducers().entrySet()) {
            FilterChip chip = new FilterChip(getContext(), pair.getKey());
            filterSelection.addProducer(pair.getKey());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    filterSelection.addProducer(pair.getValue());
                } else {
                    filterSelection.removeProducer(pair.getValue());
                }
                listener.onFilterChanged(filterSelection);
            });
            producersChipsGroup.addView(chip);
        }

        for (Map.Entry<String, String> pair : filterPresenter.getCategories().entrySet()) {
            FilterChip chip = new FilterChip(getContext(), Categories.getDescriptionByString(pair.getKey()));
            filterSelection.addCategory(pair.getKey());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    filterSelection.addCategory(pair.getValue());
                } else {
                    filterSelection.removeCategory(pair.getValue());
                }
                listener.onFilterChanged(filterSelection);
            });
            categoriesChipsGroup.addView(chip);
        }

        return view;
    }
}

class FilterChip extends Chip {
    public FilterChip(Context context, String name) {
        super(context);
        this.setText(name);
        this.setCloseIconVisible(true);
        this.setCheckedIconVisible(true);
        this.setCloseIconVisible(false);
        this.setCheckable(true);
        this.setChecked(true);
    }
}