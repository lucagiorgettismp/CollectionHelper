package com.lucagiorgetti.surprix.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.ExtraLocales;
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for showing a list of Surprises.
 *
 * Created by Luca on 28/10/2017.
 */

public class SurpRecyclerAdapter extends RecyclerView.Adapter<SurpRecyclerAdapter.SurpViewHolder>{
    private ArrayList<Surprise> surprises = new ArrayList<>();
    private Context ctx;
    public SurpRecyclerAdapter(Context context, ArrayList<Surprise> surpList){
        surprises = surpList;
        ctx = context;
    }

    @NonNull
    @Override
    public SurpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.surprise_element, parent, false);
        return new SurpViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SurpViewHolder holder, int position) {

        Surprise surp = surprises.get(position);
        holder.vCode.setText(surp.getCode());
        holder.vSetName.setText(surp.getSet_name());
        holder.vDescription.setText(surp.getDescription());
        holder.vYear.setText(String.valueOf(surp.getSet_year()));
        holder.vProducer.setText(surp.getSet_producer_name() + " " + surp.getSet_product_name());

        String nation;
        if (ExtraLocales.isExtraLocale(surp.getSet_nation())) {
           nation = ExtraLocales.getDisplayName(surp.getSet_nation());
        } else {
            Locale l = new Locale("", surp.getSet_nation());
            nation = l.getDisplayCountry();
        }

        holder.vNation.setText(nation);

        holder.vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(surp.getSet_producer_color())));

        String path = surp.getImg_path();
        if (path.startsWith("gs")){
            FirebaseStorage storage = SurprixApplication.getInstance().getFirebaseStorage();
            StorageReference gsReference = storage.getReferenceFromUrl(path);
            Glide.with(ctx).
                    load(gsReference).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_surprise_grey))
                    .into(holder.vImage);

        } else {
            Glide.with(ctx).
                    load(path).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_surprise_grey))
                    .into(holder.vImage);
        }

        Integer rarity = surp.getIntRarity();
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
        if (surprises == null){
            return 0;
        }
        return surprises.size();
    }

    public Surprise getItemAtPosition(int position) {
        return this.surprises.get(position);
    }

    public void removeItem(int position) {
        surprises.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, surprises.size());
        notifyDataSetChanged();
    }

    class SurpViewHolder extends RecyclerView.ViewHolder {
        TextView vCode;
        TextView vSetName;
        TextView vDescription;
        TextView vYear;
        TextView vProducer;
        TextView vNation;
        ImageView vImage;
        ImageView vStar1On;
        ImageView vStar2On;
        ImageView vStar3On;
        ImageView vStar1Off;
        ImageView vStar2Off;
        ImageView vStar3Off;

        View vLayout;


        SurpViewHolder(View v) {
            super(v);
            vCode = v.findViewById(R.id.txv_surp_elem_code);
            vSetName = v.findViewById(R.id.txv_surp_elem_set);
            vDescription = v.findViewById(R.id.txv_surp_elem_desc);
            vYear = v.findViewById(R.id.txv_surp_elem_year);
            vProducer = v.findViewById(R.id.txv_surp_elem_producer);
            vNation = v.findViewById(R.id.txv_surp_elem_nation);
            vImage = v.findViewById(R.id.img_surp_elem);
            vLayout = v.findViewById(R.id.layout_surp_elem_titlebar);
            vStar1On= v.findViewById(R.id.img_surp_elem_star_1_on);
            vStar2On= v.findViewById(R.id.img_surp_elem_star_2_on);
            vStar3On= v.findViewById(R.id.img_surp_elem_star_3_on);
            vStar1Off= v.findViewById(R.id.img_surp_elem_star_1_off);
            vStar2Off= v.findViewById(R.id.img_surp_elem_star_2_off);
            vStar3Off= v.findViewById(R.id.img_surp_elem_star_3_off);
        }
    }
}
