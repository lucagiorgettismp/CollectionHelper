package com.lucagiorgetti.collectionhelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lucagiorgetti.collectionhelper.Db.*;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Utente on 07/04/2017.
 */

public class SurpriseAdapter extends ArrayAdapter<Surprise> {
    private final Context context;
    private final ArrayList<Surprise> data;
    private final int layoutResourceId;
    private final DbManager manager;

    public SurpriseAdapter(Context context, int layoutResourceId, ArrayList<Surprise> data, DbManager manager) {
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
            holder.textViewCode = (TextView)row.findViewById(R.id.txv_code);
            holder.textViewDesc = (TextView)row.findViewById(R.id.txv_desc);
            holder.textViewSet = (TextView)row.findViewById(R.id.txv_set);
            holder.textViewYear = (TextView)row.findViewById(R.id.txv_year);
            holder.imgView = (ImageView)row.findViewById(R.id.imgV);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Surprise surp = data.get(position);

        holder.textViewCode.setText(surp.getCode());
        holder.textViewDesc.setText(surp.getDesc());
        holder.textViewSet.setText(manager.getSetById(surp.getSetId()).getName());
        holder.textViewYear.setText(String.valueOf(manager.getYearById(manager.getSetById(surp.getSetId()).getYearId()).getYear()));

        File imgFile = new  File(surp.getImgPath());
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

    static class ViewHolder
    {
        TextView textViewCode;
        TextView textViewDesc;
        TextView textViewSet;
        TextView textViewYear;
        ImageView imgView;
    }
}