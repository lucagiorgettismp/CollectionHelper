package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.widget.ImageView

interface SetDetailClickListener {
    fun onImageClicked(imagePath: String, imageView: ImageView, placeHolderId: Int)
}