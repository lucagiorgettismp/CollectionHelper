package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import com.lucagiorgetti.surprix.model.Surprise;

public class CollectionSurprise {
    private final boolean missing;
    private final Surprise surprise;

    public CollectionSurprise(boolean missing, Surprise surprise) {
        this.missing = missing;
        this.surprise = surprise;
    }

    public CollectionSurprise(Surprise surprise) {
        this.missing = false;
        this.surprise = surprise;
    }

    public boolean isMissing() {
        return missing;
    }

    public Surprise getSurprise() {
        return surprise;
    }
}
