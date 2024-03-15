package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import com.lucagiorgetti.surprix.model.Surprise

interface CatalogSetDetailClickListener : SetDetailClickListener {
    fun onSurpriseAddedToDoubles(s: Surprise)
    fun onSurpriseAddedToMissings(s: Surprise)
}