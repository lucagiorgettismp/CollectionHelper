package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

import com.lucagiorgetti.surprix.model.MissingDetail;
import com.lucagiorgetti.surprix.model.Surprise;

public interface SurpRecylerAdapterListener {
    void onShowMissingOwnerClick(Surprise surprise);

    void onSaveNotesClick(Surprise surprise, MissingDetail detail);

    void onDeleteNoteClick(Surprise surprise);

    void onSurpriseDelete(int position);
}