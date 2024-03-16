package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.widget.ImageView
import com.lucagiorgetti.surprix.model.Surprise

interface SetDetailClickListener {
    fun onSurpriseAddedToDoubles(surprise: Surprise)
    fun onSurpriseAddedToMissings(surprise: Surprise)
    fun onImageClicked(imagePath: String, imageView: ImageView, placeHolderId: Int)
}