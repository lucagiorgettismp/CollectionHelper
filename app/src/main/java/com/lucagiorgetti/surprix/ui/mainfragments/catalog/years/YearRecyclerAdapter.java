package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Year;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for showing a list of Years.
 * <p>
 * Created by Luca on 24/10/2017.
 */

public class YearRecyclerAdapter extends RecyclerView.Adapter<YearRecyclerAdapter.SetViewHolder> {
    private List<Year> years;

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_year, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Year year = years.get(position);
        holder.vName.setText(year.getDescr());

        /*
        //holder.vName.setTextColor(ContextCompat.getColor(SurprixApplication.getSurprixContext(), R.color.cardBackground));
        switch (position % 2) {
            case 0:
                holder.vLayout.setBackgroundColor(ContextCompat.getColor(SurprixApplication.getSurprixContext(), Colors.getHexColor(year.getProducer_color())));
                break;
            case 1:
                holder.vLayout.setBackgroundColor(ContextCompat.getColor(SurprixApplication.getSurprixContext(), Colors.getDarkHexColor(year.getProducer_color())));
                break;
        }
        */
    }

    @Override
    public int getItemCount() {
        if (years != null){
            return years.size();
        }
        return 0;
    }


    public Year getItemAtPosition(int position) {
        return this.years.get(position);
    }

    void setYears(List<Year> years) {
        Collections.sort(years, new Year.SortByDescYear());
        this.years = years;
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
