package com.lucagiorgetti.collectionhelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.lucagiorgetti.collectionhelper.model.Surprise;
import java.util.ArrayList;
/**
 * Created by Luca on 02/11/2017.
 */

public class SurpAdapter extends ArrayAdapter<Surprise> {
    private final Context context;
    private final ArrayList<Surprise> data;
    private final int layoutResourceId;

    public SurpAdapter(Context context, int layoutResourceId, ArrayList<Surprise> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
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
            holder.vCode = (TextView) row.findViewById(R.id.txv_surp_elem_code);
            holder.vSetName = (TextView) row.findViewById(R.id.txv_surp_elem_set);
            holder.vDescription = (TextView) row.findViewById(R.id.txv_surp_elem_desc);
            holder.vYear = (TextView) row.findViewById(R.id.txv_surp_elem_year);
            holder.vSeason = (TextView) row.findViewById(R.id.txv_surp_elem_season);
            holder.vProducer = (TextView) row.findViewById(R.id.txv_surp_elem_producer);
            holder.vNation = (TextView) row.findViewById(R.id.txv_surp_elem_nation);
            holder.vImage = (ImageView) row.findViewById(R.id.img_surp_elem);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Surprise surp = data.get(position);
        holder.vCode.setText(surp.getCode());
        holder.vSetName.setText(surp.getSet().getName());
        holder.vDescription.setText(surp.getDescription());
        holder.vSeason.setText(surp.getSet().getSeason());
        holder.vYear.setText(String.valueOf(surp.getSet().getYear()));
        holder.vProducer.setText(surp.getSet().getProducer().getName());
        holder.vNation.setText(surp.getSet().getNation());

        Glide.with(context).load(surp.getImg_path()).into(holder.vImage);
        return row;
    }

    public class ViewHolder {
        protected TextView vCode;
        protected TextView vSetName;
        protected TextView vDescription;
        protected TextView vSeason;
        protected TextView vYear;
        protected TextView vProducer;
        protected TextView vNation;
        protected ImageView vImage;

    }
}