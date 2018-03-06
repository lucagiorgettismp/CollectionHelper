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
import com.lucagiorgetti.surprix.model.Set;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for showing a list of Sets.
 *
 * Created by Luca on 24/10/2017.
 */

public class SetRecyclerAdapter extends RecyclerView.Adapter<SetRecyclerAdapter.SetViewHolder>{
    private ArrayList<Set> sets = new ArrayList<>();
    private ArrayList<Set> mStringFilterList;
    private Context ctx;

    public SetRecyclerAdapter(Context context, ArrayList<Set> setsList) {
        sets = setsList;
        mStringFilterList = setsList;
        ctx = context;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_element, parent,  false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.vName.setText(set.getName());

        String nation;
        if (ExtraLocales.isExtraLocale(set.getNation())) {
            nation = ExtraLocales.getDisplayName(set.getNation());
        } else {
            Locale l = new Locale("", set.getNation());
            nation = l.getDisplayCountry();
        }
        holder.vNation.setText(nation);
        holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(set.getProducer_color())));
        Glide.with(ctx).load(set.getImg_path()).into(holder.vImage);

    }

    @Override
    public int getItemCount() {
        return mStringFilterList.size();
    }

    public Set getItemAtPosition(int position) {
        return this.sets.get(position);
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vNation;
        ImageView vImage;
        View vLayout;

        SetViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txv_set_elem_name);
            vImage = (ImageView) v.findViewById(R.id.imgSet);
            vNation = (TextView) v.findViewById(R.id.txv_set_elem_nation);
            vLayout = v.findViewById(R.id.layout_set_elem_titlebar);
        }
    }
}
