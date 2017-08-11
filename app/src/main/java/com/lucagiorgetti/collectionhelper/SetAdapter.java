package com.lucagiorgetti.collectionhelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Utente on 07/04/2017.
 */

public class SetAdapter extends ArrayAdapter<Set> implements Filterable {
    private final Context context;
    private ArrayList<Set> data;
    private final int layoutResourceId;
    private final DbManager manager;
    List<Set> filterList;
    ValueFilter valueFilter;

    public SetAdapter(Context context, int layoutResourceId, ArrayList<Set> data, DbManager manager) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.manager = manager;
        this.layoutResourceId = layoutResourceId;
        this.filterList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textViewSetName = (TextView)row.findViewById(R.id.txv_setname);
            holder.textViewSeason = (TextView)row.findViewById(R.id.txv_season);
            holder.textViewProducer = (TextView)row.findViewById(R.id.txv_producer);
            holder.textViewYear = (TextView)row.findViewById(R.id.txv_setyear);
            holder.imgView = (ImageView)row.findViewById(R.id.imgSet);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Set set = data.get(position);

        holder.textViewSetName.setText(set.getName());
        String season = null;
        switch (manager.getYearById(set.getYearId()).getSeason()){
            case 0:
                season = "Estate";
                break;
            case 1:
                season = "Inverno";
        }
        holder.textViewSeason.setText(season);
        holder.textViewProducer.setText(String.valueOf(manager.getProducerById(set.getProducerId()).getName()));
        holder.textViewYear.setText(String.valueOf(manager.getYearById(set.getYearId()).getYear()));

        File imgFile = new  File(set.getImagePath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.imgView.setImageBitmap(myBitmap);
        }

        if(position %2 == 1)
        {
            row.setBackgroundResource(R.color.background_pari);
        }
        else
        {
            row.setBackgroundResource(R.color.background_dispari);
        }
        return row;
    }
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                for (int i = 0; i < filterList.size(); i++) {
                    if ((filterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(filterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            data = (ArrayList<Set>) results.values;
            notifyDataSetChanged();
        }
    }
    static class ViewHolder
    {
        TextView textViewYear;
        TextView textViewSeason;
        TextView textViewSetName;
        TextView textViewProducer;
        ImageView imgView;
    }
}