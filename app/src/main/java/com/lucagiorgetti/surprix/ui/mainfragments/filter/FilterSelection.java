package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FilterSelection {
    private final HashMap<FilterType, List<String>> selection;

    public FilterSelection() {
        selection = new HashMap<>();
        for (FilterType type : FilterType.values()){
            selection.put(type, new ArrayList<>());
        }
    }

    public void addSelection(FilterType type, String value) {
        Objects.requireNonNull(selection.get(type)).add(value);
    }

    public void removeSelection(FilterType type, String value) {
        Objects.requireNonNull(selection.get(type)).remove(value);
    }

    public List<String> getSelections(FilterType type) {
        return selection.get(type);
    }

    public boolean isSelected(FilterType type, String value){
        return selection.get(type).contains(value);
    }
}
