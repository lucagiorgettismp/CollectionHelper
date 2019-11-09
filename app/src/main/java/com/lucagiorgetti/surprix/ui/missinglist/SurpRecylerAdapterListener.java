package com.lucagiorgetti.surprix.ui.missinglist;

import com.lucagiorgetti.surprix.model.Surprise;

public interface SurpRecylerAdapterListener<T> {
    void onShowMissingOwnerClick(Surprise surprise);

}