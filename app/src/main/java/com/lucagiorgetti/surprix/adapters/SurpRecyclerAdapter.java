package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.ExtraLocales;
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for showing a list of Surprises.
 *
 * Created by Luca on 28/10/2017.
 */

public class SurpRecyclerAdapter extends RecyclerView.Adapter<SurpRecyclerAdapter.SurpViewHolder>{
    private ArrayList<Surprise> surprises = new ArrayList<>();
    private Context ctx;
    public SurpRecyclerAdapter(Context context, ArrayList<Surprise> surpList){
        surprises = surpList;
        ctx = context;
    }

    @Override
    public SurpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.surprise_element, parent, false);
        return new SurpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SurpViewHolder holder, int position) {

        Surprise surp = surprises.get(position);
        holder.vCode.setText(surp.getCode());
        holder.vSetName.setText(surp.getSet_name());
        holder.vDescription.setText(surp.getDescription());
        holder.vYear.setText(String.valueOf(surp.getSet_year()));
        holder.vProducer.setText(surp.getSet_producer_name() + " " + surp.getSet_product_name());

        String nation;
        if (ExtraLocales.isExtraLocale(surp.getSet_nation())) {
           nation = ExtraLocales.getDisplayName(surp.getSet_nation());
        } else {
            Locale l = new Locale("", surp.getSet_nation());
            nation = l.getDisplayCountry();
        }

        holder.vNation.setText(nation);

        holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(surp.getSet_producer_color())));

        Glide.with(ctx).load(surp.getImg_path()).error(R.drawable.ic_surprise).into(holder.vImage);
    }

    @Override
    public int getItemCount() {
        if (surprises == null){
            return 0;
        }
        return surprises.size();
    }

    public Surprise getItemAtPosition(int position) {
        return this.surprises.get(position);
    }

    public void removeItem(int position) {
        surprises.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, surprises.size());
        notifyDataSetChanged();
    }

    class SurpViewHolder extends RecyclerView.ViewHolder {
        TextView vCode;
        TextView vSetName;
        TextView vDescription;
        TextView vYear;
        TextView vProducer;
        TextView vNation;
        ImageView vImage;
        View vLayout;


        SurpViewHolder(View v) {
            super(v);
            vCode = (TextView) v.findViewById(R.id.txv_surp_elem_code);
            vSetName = (TextView) v.findViewById(R.id.txv_surp_elem_set);
            vDescription = (TextView) v.findViewById(R.id.txv_surp_elem_desc);
            vYear = (TextView) v.findViewById(R.id.txv_surp_elem_year);
            vProducer = (TextView) v.findViewById(R.id.txv_surp_elem_producer);
            vNation = (TextView) v.findViewById(R.id.txv_surp_elem_nation);
            vImage = (ImageView) v.findViewById(R.id.img_surp_elem);
            vLayout = v.findViewById(R.id.layout_surp_elem_titlebar);
        }
    }
}
