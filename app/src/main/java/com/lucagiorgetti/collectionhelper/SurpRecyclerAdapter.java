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
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

/**
 * Created by Luca on 28/10/2017.
 */

class SurpRecyclerAdapter extends RecyclerView.Adapter<SurpRecyclerAdapter.SurpViewHolder> implements Filterable{
    private ArrayList<Surprise> surprises = new ArrayList<>();
    ArrayList<Surprise> mStringFilterList;
    Context ctx;
    public SurpRecyclerAdapter(Context context, ArrayList<Surprise> surpList){
        surprises = surpList;
        mStringFilterList = surpList;
        ctx = context;
    }

    @Override
    public SurpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.missing_surprise_element, parent, false);
        return new SurpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SurpViewHolder holder, int position) {
        Surprise surp = surprises.get(position);
        holder.vCode.setText(surp.getCode());
        holder.vSetName.setText(surp.getSet().getName());
        holder.vDescription.setText(surp.getDescription());
        holder.vSeason.setText(surp.getSet().getSeason());
        holder.vYear.setText(String.valueOf(surp.getSet().getYear()));
        holder.vProducer.setText(surp.getSet().getProducer().getName());
        holder.vNation.setText(surp.getSet().getNation());

        Glide.with(ctx).load(surp.getImg_path()).into(holder.vImage);
    }

    @Override
    public int getItemCount() {
        return mStringFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                surprises = null;

                if (charString.isEmpty()){
                    mStringFilterList = surprises;
                } else {
                    ArrayList<Surprise> filteredList = new ArrayList<>();
                    for (Surprise surprise: surprises){
                        if (surprise.getCode().toLowerCase().contains(charString) || surprise.getDescription().toLowerCase().contains(charString)){
                            filteredList.add(surprise);
                        }
                    }
                    mStringFilterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mStringFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                surprises = (ArrayList) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SurpViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCode;
        protected TextView vSetName;
        protected TextView vDescription;
        protected TextView vSeason;
        protected TextView vYear;
        protected TextView vProducer;
        protected TextView vNation;
        protected ImageView vImage;


        public SurpViewHolder(View v) {
            super(v);
            vCode = (TextView) v.findViewById(R.id.txv_surp_elem_code);
            vSetName = (TextView) v.findViewById(R.id.txv_surp_elem_set);
            vDescription = (TextView) v.findViewById(R.id.txv_surp_elem_desc);
            vYear = (TextView) v.findViewById(R.id.txv_surp_elem_year);
            vSeason = (TextView) v.findViewById(R.id.txv_surp_elem_season);
            vProducer = (TextView) v.findViewById(R.id.txv_surp_elem_producer);
            vNation = (TextView) v.findViewById(R.id.txv_surp_elem_nation);
            vImage = (ImageView) v.findViewById(R.id.img_surp_elem);



        }
    }
}
