package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import com.lucagiorgetti.surprix.model.Set;

public class CatalogSet {
    private final Set set;
    private boolean inCollection;
    private final boolean hasMissing;

    public CatalogSet(Set set, boolean inCollection, boolean hasMissing) {
        this.inCollection = inCollection;
        this.set = set;
        this.hasMissing = hasMissing;
    }

    public CatalogSet(Set set, Boolean hasMissing) {
        this.set = set;
        this.inCollection = true;
        this.hasMissing = hasMissing;
    }

    public Set getSet() {
        return set;
    }

    public boolean isInCollection() {
        return inCollection;
    }

    public boolean hasMissing() {
        return hasMissing;
    }

    public void setInCollection(boolean inCollection) {
        this.inCollection = inCollection;
    }
}
