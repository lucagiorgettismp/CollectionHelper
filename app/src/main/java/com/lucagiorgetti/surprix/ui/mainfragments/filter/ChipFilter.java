package com.lucagiorgetti.surprix.ui.mainfragments.filter;

public class ChipFilter {
    private final String name;
    private final String value;
    private boolean selected;

    public ChipFilter(String name, String value) {
        this.name = name;
        this.value = value;
        this.selected = true;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
