package com.lucagiorgetti.surprix.ui.adapters;

import com.lucagiorgetti.surprix.model.MissingDetail;
import com.lucagiorgetti.surprix.model.Surprise;

public interface SurpRecylerAdapterListener<T> {
    void onShowMissingOwnerClick(Surprise surprise);

    void onSaveNotesClick(Surprise surprise, MissingDetail detail);

    void onDeleteNoteClick(Surprise surp);
}