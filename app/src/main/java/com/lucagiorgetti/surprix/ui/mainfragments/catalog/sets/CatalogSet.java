package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import com.lucagiorgetti.surprix.model.Set;

public class CatalogSet {
    private final boolean inCollection;
    private final Set set;

    public CatalogSet(boolean inCollection, Set set) {
        this.inCollection = inCollection;
        this.set = set;
    }

    public CatalogSet(Set set) {
        this.inCollection = true;
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

    public boolean isInCollection() {
        return inCollection;
    }
}
