package com.lucagiorgetti.surprix.ui.mainfragments.setdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;

import java.util.List;

/**
 * Adapter for showing a list of Surprises from a selected set.
 * <p>
 * Created by Luca on 28/10/2017.
 */

public class SetDetailRecyclerAdapter extends RecyclerView.Adapter<SetDetailRecyclerAdapter.SetDetailViewHolder> {

    private List<Surprise> items;

    public Surprise getItemAtPosition(int position) {
        return this.items.get(position);
    }

    @NonNull
    @Override
    public SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_set_detail, parent, false);

        return new SetDetailViewHolder(v, new SetDetailViewHolder.MyClickListener() {
            @Override
            public void onAddMissingClick(int p) {
                Surprise s = getItemAtPosition(p);
                DatabaseUtils.addMissing(s.getId());
                Snackbar.make(v, SurprixApplication.getInstance().getString(R.string.added_to_missings) + ": " + s.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> DatabaseUtils.removeMissing(s.getId())).show();

            }

            @Override
            public void onAddDoubleClick(int p) {
                Surprise s = getItemAtPosition(p);
                DatabaseUtils.addDouble(s.getId());
                Snackbar.make(v, SurprixApplication.getInstance().getString(R.string.added_to_doubles) + ": " + s.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> DatabaseUtils.removeDouble(s.getId())).show();

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailViewHolder holder, int position) {
        Surprise s = items.get(position);
        Context ctx = SurprixApplication.getSurprixContext();
        if (s.isSet_effective_code()){
            holder.vDescription.setText(s.getCode() + " - " + s.getDescription());
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

    static class SetDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View vLayout;
        TextView vDescription;
        ImageView vImage;
        ImageButton btnAddMissing;
        ImageButton btnAddDouble;
        MyClickListener listner;
        ImageView vStar1On;
        ImageView vStar2On;
        ImageView vStar3On;
        ImageView vStar1Off;
        ImageView vStar2Off;
        ImageView vStar3Off;

        SetDetailViewHolder(View v, MyClickListener listener) {
            super(v);

            this.listner = listener;
            vLayout = v.findViewById(R.id.layout_item_titlebar);
            vDescription = v.findViewById(R.id.txv_item_desc);
            vImage = v.findViewById(R.id.img_item);
            btnAddMissing = v.findViewById(R.id.btn_item_add_missing);
            btnAddDouble = v.findViewById(R.id.btn_item_add_double);
            vStar1On = v.findViewById(R.id.img_item_star_1_on);
            vStar2On = v.findViewById(R.id.img_item_star_2_on);
            vStar3On = v.findViewById(R.id.img_item_star_3_on);
            vStar1Off = v.findViewById(R.id.img_item_star_1_off);
            vStar2Off = v.findViewById(R.id.img_item_star_2_off);
            vStar3Off = v.findViewById(R.id.img_item_star_3_off);

            btnAddDouble.setOnClickListener(this);
            btnAddMissing.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_item_add_missing:
                    listner.onAddMissingClick(this.getLayoutPosition());
                    break;
                case R.id.btn_item_add_double:
                    listner.onAddDoubleClick(this.getLayoutPosition());
                    break;
            }
        }

        public interface MyClickListener {
            void onAddMissingClick(int p);

            void onAddDoubleClick(int p);
        }
    }

}
