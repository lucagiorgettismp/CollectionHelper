package com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter;

import java.util.HashMap;

public class FilterPresenter {
    private HashMap<String, String> categories;
    private HashMap<String, String> producers;
    private HashMap<String, String> years;

    public FilterPresenter(HashMap<String, String> categories, HashMap<String, String> producers, HashMap<String, String> years) {
        this.categories = categories;
        this.producers = producers;
        this.years = years;
    }

    public HashMap<String, String> getCategories() {
        return categories;
    }

    public HashMap<String, String> getProducers() {
        return producers;
    }

    public HashMap<String, String> getYears() {
        return years;
    }
}
