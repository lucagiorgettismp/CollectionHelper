package com.lucagiorgetti.surprix.ui.mainfragments.filter

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

internal class BottomSheetChip : Chip {
    constructor(context: Context?, chipFilter: ChipFilter?) : super(context) {
        this.text = chipFilter?.name
        this.isCloseIconVisible = true
        this.isCheckedIconVisible = true
        this.isCloseIconVisible = false
        this.isCheckable = true
        this.isChecked = chipFilter!!.isSelected
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
}
