package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.StarRank;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import java.util.List;
import java.util.Locale;

/**
 * Adapter for showing a list of Surprises from a selected set.
 * <p>
 * Created by Luca on 28/10/2017.
 */

public class CollectionSetDetailRecyclerAdapter extends BaseSetDetailAdapter<CollectionSetDetailRecyclerAdapter.SetDetailViewHolder> {

    private List<CollectionSurprise> items;

    public CollectionSurprise getItemAtPosition(int position) {
        return this.items.get(position);
    }

    @NonNull
    @Override
    public SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_collection_set_detail, parent, false);

        return new SetDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailViewHolder holder, int position) {
        Surprise s = items.get(position).getSurprise();
        Context ctx = SurprixApplication.getSurprixContext();
        if (s.isSet_effective_code()) {
            holder.vDescription.setText(String.format(Locale.getDefault(), "%s - %s", s.getCode(), s.getDescription()));
        } else {
            holder.vDescription.setText(s.getDescription());
        }

        String path = s.getImg_path();
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_logo_shape_primary);

        Integer rarity = s.getIntRarity();

        holder.vStarRank.setValue(rarity);

        if (items.get(position).isMissing()) {
            holder.miss.setVisibility(View.VISIBLE);
            holder.check.setVisibility(View.GONE);
        } else {
            holder.miss.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void setSurprises(List<CollectionSurprise> surprises) {
        this.items = surprises;
    }

    static class SetDetailViewHolder extends RecyclerView.ViewHolder {
        TextView vDescription;
        ImageView vImage;
        StarRank vStarRank;
        ImageView miss;
        ImageView check;

        SetDetailViewHolder(View v) {
            super(v);

            vDescription = v.findViewById(R.id.txv_item_desc);
            vImage = v.findViewById(R.id.img_item);
            vStarRank = v.findViewById(R.id.star_rank);
            miss = v.findViewById(R.id.set_detail_miss);
            check = v.findViewById(R.id.set_detail_check);
        }
    }

}
