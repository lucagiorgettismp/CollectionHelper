package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Year;

import java.util.ArrayList;

/**
 * Adapter for showing a list of Years.
 * <p>
 * Created by Luca on 24/10/2017.
 */

public class YearRecyclerAdapter extends RecyclerView.Adapter<YearRecyclerAdapter.SetViewHolder> {
    private ArrayList<Year> years = new ArrayList<>();
    private Context ctx;

    public YearRecyclerAdapter(Context context, ArrayList<Year> yearsList) {
        years = yearsList;
        ctx = context;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_element, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Year year = years.get(position);
        holder.vName.setText(year.getDescr());

        holder.vName.setTextColor(ContextCompat.getColor(ctx, R.color.cardBackground));
        switch (position % 2) {
            case 0:
                holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(year.getProducer_color())));
                break;
            case 1:
                holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getDarkHexColor(year.getProducer_color())));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return years.size();
    }


    public Year getItemAtPosition(int position) {
        return this.years.get(position);
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        View vLayout;

        SetViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txv_year_number);
            vLayout = v.findViewById(R.id.layout_year_select);
        }
    }
}
