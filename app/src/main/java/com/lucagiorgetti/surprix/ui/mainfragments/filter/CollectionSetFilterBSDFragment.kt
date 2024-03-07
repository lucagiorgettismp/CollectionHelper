package com.lucagiorgetti.surprix.ui.mainfragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import com.lucagiorgetti.surprix.R

class CollectionSetFilterBSDFragment(private val chipFilters: ChipFilters?) : BottomSheetDialogFragment() {
    private var listener: FilterBottomSheetListener? = null
    fun setListener(listener: FilterBottomSheetListener?) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_collection_set, container,
                false)
        val resetFilters = view.findViewById<Button>(R.id.filter_reset)
        resetFilters.setOnClickListener { listener!!.onFilterCleared() }
        val completionChipsGroup = view.findViewById<ChipGroup>(R.id.filter_category_completion)
        for (type in FilterType.getValues(FilterSelectableType.CATALOG_SET)) {
            for (filter in chipFilters!!.getFiltersByType(type).values) {
                val chip = BottomSheetChip(context, filter)
                chip.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    chipFilters.setFilterSelection(type, filter?.value, isChecked)
                    listener!!.onFilterChanged(chipFilters)
                }
                if (type == FilterType.COMPLETION) {
                    completionChipsGroup.addView(chip)
                }
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        val bottomSheet = dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}