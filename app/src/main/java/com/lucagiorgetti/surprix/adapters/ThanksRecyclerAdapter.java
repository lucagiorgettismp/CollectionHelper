package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.Sponsor;

import java.util.ArrayList;

/**
 * Adapter for showing a list of Sponsor.
 *
 * Created by Luca on 24/10/2017.
 */

public class ThanksRecyclerAdapter extends RecyclerView.Adapter<ThanksRecyclerAdapter.SetViewHolder>{
    private ArrayList<Sponsor> sponsorsList = new ArrayList<>();
    private Context ctx;

    public ThanksRecyclerAdapter(Context context, ArrayList<Sponsor> sponsorsList) {
        this.sponsorsList = sponsorsList;
        ctx = context;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.thanks_element, parent,  false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        Sponsor sponsor = sponsorsList.get(position);

        holder.vImage.setImageDrawable(sponsor.getBanner());
    }

    @Override
    public int getItemCount() {
        return sponsorsList.size();
    }

    public Sponsor getItemAtPosition(int position) {
        return this.sponsorsList.get(position);
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        ImageView vImage;

        SetViewHolder(View v) {
            super(v);
            vImage =  (ImageView) v.findViewById(R.id.image_thanks);
        }
    }
}
