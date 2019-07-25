package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
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
import com.lucagiorgetti.surprix.model.Set;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for showing a list of Sets.
 * <p>
 * Created by Luca on 24/10/2017.
 */

public class SetRecyclerAdapter extends RecyclerView.Adapter<SetRecyclerAdapter.SetViewHolder> {
    private ArrayList<Set> sets;
    private ArrayList<Set> mStringFilterList;
    private Context ctx;

    public SetRecyclerAdapter(Context context, ArrayList<Set> setsList) {
        sets = setsList;
        mStringFilterList = setsList;
        ctx = context;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_element, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
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

        String path = set.getImg_path();
        if (path.startsWith("gs")) {
            FirebaseStorage storage = SurprixApplication.getInstance().getFirebaseStorage();
            StorageReference gsReference = storage.getReferenceFromUrl(path);
            Glide.with(ctx).
                    load(gsReference).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_bpz_placeholder))
                    .into(holder.vImage);

        } else {
            Glide.with(ctx).
                    load(path).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_bpz_placeholder))
                    .into(holder.vImage);
        }
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
            vName = v.findViewById(R.id.txv_set_elem_name);
            vImage = v.findViewById(R.id.imgSet);
            vNation = v.findViewById(R.id.txv_set_elem_nation);
            vLayout = v.findViewById(R.id.layout_set_elem_titlebar);
        }
    }
}
