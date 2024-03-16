package com.lucagiorgetti.surprix.ui.mainfragments.filter

import com.lucagiorgetti.surprix.model.Categories
import com.lucagiorgetti.surprix.model.Surprise

class ChipFilters {
    private var filters: HashMap<FilterType, HashMap<String?, ChipFilter?>>? = null
    fun initBySurprises(surprises: List<Surprise?>) {
        val categories = HashMap<String?, ChipFilter?>()
        val producers = HashMap<String?, ChipFilter?>()
        val years = HashMap<String?, ChipFilter?>()
        for (surprise in surprises) {
            if (!categories.containsKey(surprise?.set_category)) {
                categories[surprise?.set_category] = ChipFilter(Categories.getDescriptionByString(surprise?.set_category), surprise?.set_category)
            }
            if (!producers.containsKey(surprise?.set_producer_name)) {
                producers[surprise?.set_producer_name] = ChipFilter(surprise?.set_producer_name, surprise?.set_producer_name)
            }
            if (!years.containsKey(surprise?.set_year_name)) {
                years[surprise?.set_year_year.toString()] = ChipFilter(surprise?.set_year_year.toString(), surprise?.set_year_year.toString())
            }
        }
        filters = HashMap()
        filters!![FilterType.CATEGORY] = categories
        filters!![FilterType.PRODUCER] = producers
        filters!![FilterType.YEAR] = years
    }

    fun getFiltersByType(type: FilterType): HashMap<String?, ChipFilter?> {
        return filters!![type]!!
    }

    fun setFilterSelection(type: FilterType, value: String?, selected: Boolean) {
        filters!![type]!![value]?.isSelected = selected
    }

    fun clearSelection() {
        for (type in FilterType.getValues(FilterSelectableType.SURPRISE)) {
            for (chipFilter in filters!![type]!!.values) {
                chipFilter?.isSelected = true
            }
        }
    }
}
