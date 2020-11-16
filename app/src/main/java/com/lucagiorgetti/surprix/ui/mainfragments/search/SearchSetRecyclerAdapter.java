package com.lucagiorgetti.surprix.ui.mainfragments.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Locale;

/**
 * Adapter for showing a list of Sets.
 * <p>
 * Created by Luca on 24/10/2017.
 */

public class SearchSetRecyclerAdapter extends ListAdapter<Set, SearchSetRecyclerAdapter.SetViewHolder> implements Filterable {
    private List<Set> filterableList;

    SearchSetRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Set> DIFF_CALLBACK = new DiffUtil.ItemCallback<Set>() {
        @Override
        public boolean areItemsTheSame(@NonNull Set oldItem, @NonNull Set newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Set oldItem, @NonNull Set newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_set_search, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Set set = getItem(position);
        holder.vName.setText(set.getName());
        Context ctx = SurprixApplication.getSurprixContext();

        String nation;
        if (ExtraLocales.isExtraLocale(set.getNation())) {
            nation = ExtraLocales.getDisplayName(set.getNation());
        } else {
            Locale l = new Locale("", set.getNation());
            nation = l.getDisplayCountry();
        }
        holder.vNation.setText(nation);
        holder.vProducer.setText(set.getProducer_name());
        holder.vYear.setText(set.getYear_desc());
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

    public Set getItemAtPosition(int position) {
        return getItem(position);
    }

    void setFilterableList(List<Set> sets) {
        this.filterableList = sets;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Set> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(filterableList);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();

                for (Set set: filterableList){
                    if (set.getCode().toLowerCase().contains(pattern)
                            || set.getName().toLowerCase().contains(pattern)){
                        filteredList.add(set);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            submitList((List<Set>) filterResults.values);
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vNation;
        TextView vProducer;
        TextView vYear;
        ImageView vImage;
        View vLayout;

        SetViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txv_set_elem_name);
            vImage = v.findViewById(R.id.imgSet);
            vNation = v.findViewById(R.id.txv_set_elem_nation);
            vProducer = v.findViewById(R.id.txv_set_elem_producer);
            vYear = v.findViewById(R.id.txv_set_elem_year);
            vLayout = v.findViewById(R.id.set_divider);
        }
    }
}
