package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

import com.lucagiorgetti.surprix.model.Surprise;

public interface SurpRecylerAdapterListener {
    void onShowMissingOwnerClick(Surprise surprise);

    void onSurpriseDelete(int position);
}