package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList

import android.widget.ImageView

interface BaseSurpriseRecyclerAdapterListener {
    fun onSurpriseDelete(position: Int)
    fun onImageClicked(imagePath: String, imageView: ImageView, placeHolderId: Int)
}