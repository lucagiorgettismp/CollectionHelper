package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Categories;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets.CatalogSet;

import java.util.HashMap;
import java.util.List;

public class ChipFilters {

    private HashMap<FilterType, HashMap<String, ChipFilter>> filters;
    public static final String COMPLETION_COMPLETED = "complete";
    public static final String COMPLETION_NON_COMPLETED = "non_complete";

    public void initBySurprises(List<Surprise> surprises) {
        HashMap<String, ChipFilter> categories = new HashMap<>();
        HashMap<String, ChipFilter> producers = new HashMap<>();
        HashMap<String, ChipFilter> years = new HashMap<>();

        for (Surprise surprise : surprises) {
            if (!categories.containsKey(surprise.getSet_category())) {
                categories.put(surprise.getSet_category(), new ChipFilter(Categories.getDescriptionByString(surprise.getSet_category()), surprise.getSet_category()));
            }

            if (!producers.containsKey(surprise.getSet_producer_name())) {
                producers.put(surprise.getSet_producer_name(), new ChipFilter(surprise.getSet_producer_name(), surprise.getSet_producer_name()));

            }

            if (!years.containsKey(surprise.getSet_year_name())) {
                years.put(String.valueOf(surprise.getSet_year_year()), new ChipFilter(String.valueOf(surprise.getSet_year_year()), String.valueOf(surprise.getSet_year_year())));
            }
        }

        filters = new HashMap<>();
        filters.put(FilterType.CATEGORY, categories);
        filters.put(FilterType.PRODUCER, producers);
        filters.put(FilterType.YEAR, years);
    }

    public void initByCatalogSets(List<CatalogSet> sets) {
        HashMap<String, ChipFilter> completionValues = new HashMap<>();

        for (CatalogSet set : sets) {
            String completionString = set.hasMissing() ? COMPLETION_NON_COMPLETED : COMPLETION_COMPLETED;
            String label = set.hasMissing() ? SurprixApplication.getInstance().getString(R.string.incomplete) : SurprixApplication.getInstance().getString(R.string.complete);

            if (!completionValues.containsKey(completionString)) {
                completionValues.put(completionString, new ChipFilter(label, completionString));
            }
        }

        filters = new HashMap<>();
        filters.put(FilterType.COMPLETION, completionValues);
    }


    public HashMap<String, ChipFilter> getFiltersByType(FilterType type) {
        return filters.get(type);
    }

    public boolean isSelected(FilterType type, String value) {
        return filters.get(type).get(value).isSelected();
    }

    public void setFilterSelection(FilterType type, String value, boolean selected) {
        filters.get(type).get(value).setSelected(selected);
    }

    public void clearSelection() {
        for (FilterType type : FilterType.values()) {
            for (ChipFilter chipFilter : filters.get(type).values()) {
                chipFilter.setSelected(true);
            }
        }
    }

}
