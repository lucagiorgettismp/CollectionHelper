package com.lucagiorgetti.surprix.ui.mainfragments.setdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.List;
import java.util.Locale;

/**
 * Adapter for showing a list of Surprises from a selected set.
 * <p>
 * Created by Luca on 28/10/2017.
 */

public class SetDetailRecyclerAdapter extends RecyclerView.Adapter<SetDetailRecyclerAdapter.SetDetailViewHolder> {

    private List<Surprise> items;
    private SetDetailFragment.MyClickListener listener;

    public Surprise getItemAtPosition(int position) {
        return this.items.get(position);
    }

    @NonNull
    @Override
    public SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_set_detail, parent, false);

        return new SetDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailViewHolder holder, int position) {
        Surprise s = items.get(position);
        Context ctx = SurprixApplication.getSurprixContext();
        if (s.isSet_effective_code()) {
            holder.vDescription.setText(String.format(Locale.getDefault(), "%s - %s", s.getCode(), s.getDescription()));
        } else {
            holder.vDescription.setText(s.getDescription());
        }
        holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(s.getSet_producer_color())));

        String path = s.getImg_path();
        if (path.startsWith("gs")) {
            FirebaseStorage storage = SurprixApplication.getInstance().getFirebaseStorage();
            StorageReference gsReference = storage.getReferenceFromUrl(path);
            Glide.with(ctx).
                    load(gsReference).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_logo_shape_primary))
                    .into(holder.vImage);

        } else {
            Glide.with(ctx).
                    load(s.getImg_path()).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_logo_shape_primary))
                    .into(holder.vImage);
        }

        Integer rarity = s.getIntRarity();
        holder.vStar1On.setVisibility(View.GONE);
        holder.vStar2On.setVisibility(View.GONE);
        holder.vStar3On.setVisibility(View.GONE);
        holder.vStar1Off.setVisibility(View.VISIBLE);
        holder.vStar2Off.setVisibility(View.VISIBLE);
        holder.vStar3Off.setVisibility(View.VISIBLE);
        if (rarity != null) {
            switch (rarity) {
                case 1:
                    holder.vStar1On.setVisibility(View.VISIBLE);
                    holder.vStar1Off.setVisibility(View.GONE);
                    break;
                case 2:
                    holder.vStar1On.setVisibility(View.VISIBLE);
                    holder.vStar2On.setVisibility(View.VISIBLE);
                    holder.vStar1Off.setVisibility(View.GONE);
                    holder.vStar2Off.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.vStar1On.setVisibility(View.VISIBLE);
                    holder.vStar2On.setVisibility(View.VISIBLE);
                    holder.vStar3On.setVisibility(View.VISIBLE);
                    holder.vStar1Off.setVisibility(View.GONE);
                    holder.vStar2Off.setVisibility(View.GONE);
                    holder.vStar3Off.setVisibility(View.GONE);
                    break;
            }
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSurpriseAdded(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void setSurprises(List<Surprise> surprises) {
        this.items = surprises;
    }

    public void setListener(SetDetailFragment.MyClickListener myClickListener) {
        this.listener = myClickListener;
    }

    static class SetDetailViewHolder extends RecyclerView.ViewHolder {
        View vLayout;
        TextView vDescription;
        ImageView vImage;
        Button add;
        ImageView vStar1On;
        ImageView vStar2On;
        ImageView vStar3On;
        ImageView vStar1Off;
        ImageView vStar2Off;
        ImageView vStar3Off;


        SetDetailViewHolder(View v) {
            super(v);

            vLayout = v.findViewById(R.id.layout_item_titlebar);
            vDescription = v.findViewById(R.id.txv_item_desc);
            vImage = v.findViewById(R.id.img_item);
            add = v.findViewById(R.id.add_btn);
            vStar1On = v.findViewById(R.id.img_item_star_1_on);
            vStar2On = v.findViewById(R.id.img_item_star_2_on);
            vStar3On = v.findViewById(R.id.img_item_star_3_on);
            vStar1Off = v.findViewById(R.id.img_item_star_1_off);
            vStar2Off = v.findViewById(R.id.img_item_star_2_off);
            vStar3Off = v.findViewById(R.id.img_item_star_3_off);
        }
    }

}
