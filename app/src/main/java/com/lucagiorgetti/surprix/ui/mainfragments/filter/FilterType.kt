package com.lucagiorgetti.surprix.ui.mainfragments.filter

import java.util.Arrays

enum class FilterType {
    CATEGORY,
    PRODUCER,
    YEAR,
    COMPLETION;

    companion object {
        fun getValues(selectableType: FilterSelectableType?): List<FilterType> {
            when (selectableType) {
                FilterSelectableType.CATALOG_SET -> return listOf(COMPLETION)
                FilterSelectableType.SURPRISE -> return Arrays.asList(CATEGORY, PRODUCER, YEAR)
                else -> {}
            }
            return ArrayList()
        }
    }
}
