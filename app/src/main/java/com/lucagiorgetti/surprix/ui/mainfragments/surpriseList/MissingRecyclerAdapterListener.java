package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList;

import com.lucagiorgetti.surprix.model.Surprise;

public interface MissingRecyclerAdapterListener extends BaseSurpriseRecyclerAdapterListener{
    void onShowMissingOwnerClick(Surprise surprise);
}