package com.lucagiorgetti.surprix.ui.mainfragments.filter

interface FilterBottomSheetListener {
    fun onFilterChanged(selection: ChipFilters?)
    fun onFilterCleared()
}
