package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import androidx.recyclerview.widget.RecyclerView

abstract class BaseSetDetailAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    abstract fun setSurprises(surprises: List<CollectionSurprise>)
}
