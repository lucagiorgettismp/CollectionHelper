package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import android.widget.ImageView;

public interface SetDetailClickListener {

    void onImageClicked(String imagePath, ImageView imageView, int placeHolderId);
}