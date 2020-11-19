package com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterSelection {
    private List<String> categories = new ArrayList<>();
    private List<String> producers = new ArrayList<>();
    private List<String> years = new ArrayList<>();

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getProducers() {
        return producers;
    }

    public List<String> getYears() {
        return years;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public void addProducer(String producer) {
        this.producers.add(producer);
    }

    public void addYear(String year) {
        this.years.add(year);
    }

    public void removeCategory(String category) {
        this.categories.remove(category);
    }

    public void removeProducer(String producer) {
        this.producers.remove(producer);
    }

    public void removeYear(String year) {
        this.years.remove(year);
    }
}
