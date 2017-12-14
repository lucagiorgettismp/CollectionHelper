package com.lucagiorgetti.collectionhelper.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.model.Colors;
import com.lucagiorgetti.collectionhelper.model.Producer;

import java.util.ArrayList;

/**
 * Adapter for showing a list of Producers.
 *
 * Created by Luca on 24/10/2017.
 */

public class ProducerRecyclerAdapter extends RecyclerView.Adapter<ProducerRecyclerAdapter.SetViewHolder>{
    private ArrayList<Producer> producers = new ArrayList<>();
    private ArrayList<Producer> mFilterList;
    private Context ctx;

    public ProducerRecyclerAdapter(Context context, ArrayList<Producer> producersList) {
        producers = producersList;
        mFilterList = producersList;
        ctx = context;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_select_element, parent,  false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        Producer producer = producers.get(position);
        holder.vName.setText(producer.getName());
        holder.vProduct.setText(producer.getProduct());

        holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(producer.getColor())));
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public Producer getItemAtPosition(int position) {
        return this.producers.get(position);
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vProduct;
        View vLayout;

        SetViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txv_title_prd_select);
            vProduct = (TextView)  v.findViewById(R.id.txv_subtitle_prd_select);
            vLayout = v.findViewById(R.id.layout_prd_select);
        }
    }
}
