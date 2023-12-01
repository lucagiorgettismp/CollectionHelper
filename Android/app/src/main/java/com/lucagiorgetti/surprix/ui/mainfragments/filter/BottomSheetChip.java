package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.chip.Chip;

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
