package com.lucagiorgetti.surprix.ui.mainfragments.filter

enum class FilterType {
    CATEGORY,
    PRODUCER,
    YEAR,
    COMPLETION;

    companion object {
        fun getValues(selectableType: FilterSelectableType?): List<FilterType> {
            when (selectableType) {
                FilterSelectableType.CATALOG_SET -> return listOf(COMPLETION)
                FilterSelectableType.SURPRISE -> return listOf(CATEGORY, PRODUCER, YEAR)
                else -> {}
            }
            return ArrayList()
        }
    }
}
