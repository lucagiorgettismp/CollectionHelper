package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList;

import android.widget.ImageView;

public interface BaseSurpriseRecyclerAdapterListener {
    void onSurpriseDelete(int position);

    void onImageClicked(String imagePath, ImageView imageView, int placeHolderId);
}