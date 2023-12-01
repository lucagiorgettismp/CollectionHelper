package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
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

public class CatalogSetDetailRecyclerAdapter extends BaseSetDetailAdapter<CatalogSetDetailRecyclerAdapter.SetDetailViewHolder> {

    private List<CollectionSurprise> items;
    private final CatalogSetDetailClickListener listener;

    public CatalogSetDetailRecyclerAdapter(CatalogSetDetailClickListener myClickListener) {
        listener = myClickListener;
    }

    public CollectionSurprise getItemAtPosition(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_set_detail, parent, false);
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
        holder.vAddMissing.setOnClickListener(v -> listener.onSurpriseAddedToMissings(s));
        holder.vAddDouble.setOnClickListener(v -> listener.onSurpriseAddedToDoubles(s));
        holder.vImage.setOnClickListener(v -> listener.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary));
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
        MaterialButton vAddMissing;
        MaterialButton vAddDouble;
        StarRank vStarRank;

        SetDetailViewHolder(View v) {
            super(v);

            vDescription = v.findViewById(R.id.txv_item_desc);
            vImage = v.findViewById(R.id.img_item);
            vAddMissing = v.findViewById(R.id.add_missing_btn);
            vAddDouble = v.findViewById(R.id.add_double_btn);
            vStarRank = v.findViewById(R.id.star_rank);
        }
    }

}
