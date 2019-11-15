package com.lucagiorgetti.surprix.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Producer;

import java.util.List;

/**
 * Adapter for showing a list of Producers.
 * <p>
 * Created by Luca on 24/10/2017.
 */

public class CatalogRecyclerAdapter extends RecyclerView.Adapter<CatalogRecyclerAdapter.SetViewHolder> {
    private List<Producer> producers;

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_catalog, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Producer producer = producers.get(position);
        holder.vName.setText(producer.getName());
        String product = producer.getProduct();
        if (product != null && !product.equals("")){
            holder.vProduct.setText(producer.getProduct());
        } else {
            holder.vProduct.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (producers == null) {
            return 0;
        }
        return producers.size();
    }

    public Producer getItemAtPosition(int position) {
        return this.producers.get(position);
    }

    public void setYears(List<Producer> producers) {
        this.producers = producers;
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vProduct;
        View vLayout;

        SetViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txv_title_prd_select);
            vProduct = v.findViewById(R.id.txv_subtitle_prd_select);
            vLayout = v.findViewById(R.id.layout_prd_select);
        }
    }
}
