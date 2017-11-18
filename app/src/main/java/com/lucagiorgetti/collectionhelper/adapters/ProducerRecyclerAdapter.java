package com.lucagiorgetti.collectionhelper.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.model.Colors;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.Set;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Luca on 24/10/2017.
 */

public class ProducerRecyclerAdapter extends RecyclerView.Adapter<ProducerRecyclerAdapter.SetViewHolder>{
    private ArrayList<Producer> producers = new ArrayList<>();
    ArrayList<Producer> mFilterList;
    Context ctx;

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

        String color = null;
        switch (position % 5){
            case 0:
                color = Colors.BLUE;
                break;
            case 1:
                color = Colors.GREEN;
                break;
            case 2:
                color = Colors.ORANGE;
                break;
            case 3:
                color = Colors.RED;
                break;
            case 4:
                color = Colors.PURPLE;
                break;
        }
        holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(color)));
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public Producer getItemAtPosition(int position) {
        return this.producers.get(position);
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vProduct;
        protected View vLayout;

        public SetViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txv_title_prd_select);
            vProduct = (TextView)  v.findViewById(R.id.txv_subtitle_prd_select);
            vLayout = (View) v.findViewById(R.id.layout_prd_select);

        }
    }
}
