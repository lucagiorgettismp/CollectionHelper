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
import java.util.Locale;

/**
 * Created by Utente on 07/04/2017.
 */

public class SetAdapter extends ArrayAdapter<Set> implements Filterable {
    private final Context context;
    private ArrayList<Set> data;
    private ArrayList<Set> arraylist;
    private final int layoutResourceId;
    private final DbManager manager;

    public SetAdapter(Context context, int layoutResourceId, ArrayList<Set> data, DbManager manager) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.manager = manager;
        this.layoutResourceId = layoutResourceId;
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
        /*
        switch (manager.getYearById(set.getYearId()).getSeason()){
            case 0:
                season = "Estate";
                break;
            case 1:
                season = "Inverno";
        }
        */

        holder.textViewSeason.setText("culo");
        holder.textViewProducer.setText("culo");
        holder.textViewYear.setText("culo");

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

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ArrayList <Set> arraylist = new ArrayList<>();
        arraylist = this.manager.getSets();
        this.data.clear();
        if (charText.length() == 0) {
            this.data.addAll(arraylist);
        } else {
            for (Set s : arraylist) {
                if (s.getName().toUpperCase().contains(charText.toUpperCase())) {
                    this.data.add(s);
                }
            }
        }
        notifyDataSetChanged();
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