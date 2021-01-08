package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseSetDetailAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    abstract void setSurprises(List<CollectionSurprise> surprises);
}
