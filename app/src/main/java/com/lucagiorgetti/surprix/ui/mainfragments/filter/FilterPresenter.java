package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import com.lucagiorgetti.surprix.model.Surprise;

import java.util.HashMap;
import java.util.List;

public class FilterPresenter {
    private final HashMap<FilterType, HashMap<String, String>> filters;

    public FilterPresenter(List<Surprise> surprises) {
        HashMap<String, String> categories = new HashMap<>();
        HashMap<String, String> producers = new HashMap<>();
        HashMap<String, String> years = new HashMap<>();

        for (Surprise surprise : surprises) {
            if (!categories.containsKey(surprise.getSet_category())) {
                categories.put(surprise.getSet_category(), surprise.getSet_category());
            }

            if (!producers.containsKey(surprise.getSet_producer_name())) {
                producers.put(surprise.getSet_producer_name(), surprise.getSet_producer_name());
            }

            if (!years.containsKey(surprise.getSet_year_name())) {
                years.put(String.valueOf(surprise.getSet_year_year()), String.valueOf(surprise.getSet_year_year()));
            }
        }

        filters = new HashMap<>();
        filters.put(FilterType.CATEGORY, categories);
        filters.put(FilterType.PRODUCER, producers);
        filters.put(FilterType.YEAR, years);
    }

    public HashMap<String, String> getByType(FilterType type) {
        return filters.get(type);
    }
}
