package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList

import com.lucagiorgetti.surprix.model.Surprise

interface MissingRecyclerAdapterListener : BaseSurpriseRecyclerAdapterListener {
    fun onShowMissingOwnerClick(surprise: Surprise)
}