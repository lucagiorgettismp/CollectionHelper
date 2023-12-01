package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.ChipGroup;
import com.lucagiorgetti.surprix.R;

public class CollectionSetFilterBSDFragment extends BottomSheetDialogFragment {
    private final ChipFilters chipFilters;

    private FilterBottomSheetListener listener;

    public CollectionSetFilterBSDFragment(ChipFilters chipFilters) {
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

        View view = inflater.inflate(R.layout.bottom_sheet_collection_set, container,
                false);

        Button resetFilters = view.findViewById(R.id.filter_reset);
        resetFilters.setOnClickListener(v -> listener.onFilterCleared());

        ChipGroup completionChipsGroup = view.findViewById(R.id.filter_category_completion);

        for (FilterType type : FilterType.getValues(FilterSelectableType.CATALOG_SET)) {
            for (ChipFilter filter : chipFilters.getFiltersByType(type).values()) {
                BottomSheetChip chip = new BottomSheetChip(getContext(), filter);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    chipFilters.setFilterSelection(type, filter.getValue(), isChecked);
                    listener.onFilterChanged(chipFilters);
                });

                if (type == FilterType.COMPLETION) {
                    completionChipsGroup.addView(chip);
                }
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}