package com.lucagiorgetti.surprix.ui.mainfragments.filter;

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

import java.util.Map;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private final FilterSelection startingFilterSelection;

    private final FilterPresenter filterPresenter;
    private FilterBottomSheetListener listener;

    public FilterBottomSheetDialogFragment(FilterPresenter filterPresenter, FilterSelection filterSelection) {
        this.filterPresenter = filterPresenter;
        this.startingFilterSelection = filterSelection;
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

        FilterSelection filterSelection = new FilterSelection();

        for (FilterType type : FilterType.values()) {
            for (Map.Entry<String, String> pair : filterPresenter.getByType(type).entrySet()) {
                FilterChip chip = new FilterChip(getContext(), pair.getKey(), startingFilterSelection == null || startingFilterSelection.isSelected(type, pair.getValue()));
                filterSelection.addSelection(type, pair.getKey());
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        filterSelection.addSelection(type, pair.getValue());
                    } else {
                        filterSelection.removeSelection(type, pair.getValue());
                    }
                    listener.onFilterChanged(filterSelection);
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

class FilterChip extends Chip {
    public FilterChip(Context context, String name, boolean checked) {
        super(context);
        this.setText(name);
        this.setCloseIconVisible(true);
        this.setCheckedIconVisible(true);
        this.setCloseIconVisible(false);
        this.setCheckable(true);
        this.setChecked(checked);
    }
}