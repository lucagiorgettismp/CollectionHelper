package com.lucagiorgetti.collectionhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucagiorgetti.collectionhelper.model.Set;

import java.util.ArrayList;

/**
 * Created by Luca on 24/10/2017.
 */

public class SetRecyclerAdapter extends RecyclerView.Adapter<SetRecyclerAdapter.SetViewHolder>{
    private ArrayList<Set> sets = new ArrayList<>();
    ArrayList<Set> mStringFilterList;
    Context ctx;

    public SetRecyclerAdapter(Context context, ArrayList<Set> setsList) {
        sets = setsList;
        mStringFilterList = setsList;
        ctx = context;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_sets_element, parent,  false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.vName.setText(set.getName());
        holder.vProducer.setText(set.getProducer().getName());
        holder.vYear.setText(String.valueOf(set.getYear()));
        holder.vSeason.setText(set.getSeason());
        holder.vNation.setText(set.getNation());

        Glide.with(ctx).load(set.getImg_path()).into(holder.vImage);

    }

    @Override
    public int getItemCount() {
        return mStringFilterList.size();
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vSeason;
        protected TextView vYear;
        protected TextView vProducer;
        protected TextView vNation;
        protected ImageView vImage;

        public SetViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txv_set_elem_name);
            vSeason = (TextView)  v.findViewById(R.id.txv_set_elem_season);
            vYear = (TextView)  v.findViewById(R.id.txv_set_elem_year);
            vProducer = (TextView) v.findViewById(R.id.txv_set_elem_producer);
            vImage = (ImageView) v.findViewById(R.id.imgSet);
            vNation = (TextView) v.findViewById(R.id.txv_set_elem_nation);
        }
    }
}
