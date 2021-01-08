package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.ExtraLocales;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for showing a list of Sets.
 * <p>
 * Created by Luca on 24/10/2017.
 */

public class SetRecyclerAdapter extends ListAdapter<CatalogSet, SetRecyclerAdapter.SetViewHolder> implements Filterable {
    private final SetListFragment.MyClickListener listener;
    private List<CatalogSet> filterableList;
    private CatalogNavigationMode navigationMode;

    public SetRecyclerAdapter(CatalogNavigationMode navigationMode, SetListFragment.MyClickListener myClickListener) {
        super(DIFF_CALLBACK);
        this.navigationMode = navigationMode;
        this.listener = myClickListener;
    }

    private static final DiffUtil.ItemCallback<CatalogSet> DIFF_CALLBACK = new DiffUtil.ItemCallback<CatalogSet>() {
        @Override
        public boolean areItemsTheSame(@NonNull CatalogSet oldItem, @NonNull CatalogSet newItem) {
            return oldItem.getSet().getId().equals(newItem.getSet().getId()) && newItem.isInCollection() == oldItem.isInCollection();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CatalogSet oldItem, @NonNull CatalogSet newItem) {
            return oldItem.getSet().getId().equals(newItem.getSet().getId()) && newItem.isInCollection() == oldItem.isInCollection();
        }
    };

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_set, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Set set = getItem(position).getSet();
        boolean init = true;

        holder.vName.setText(set.getName());

        String nation;
        if (ExtraLocales.isExtraLocale(set.getNation())) {
            nation = ExtraLocales.getDisplayName(set.getNation());
        } else {
            Locale l = new Locale("", set.getNation());
            nation = l.getDisplayCountry();
        }
        holder.vNation.setText(nation);

        if (navigationMode.equals(CatalogNavigationMode.COLLECTION)) {
            holder.myCollectionActions.setVisibility(View.GONE);
        } else {
            holder.myCollectionActions.setVisibility(View.VISIBLE);

            holder.myCollectionSwitch.setOnClickListener(v -> {
                boolean isChecked =  holder.myCollectionSwitch.isChecked();
                listener.onSetInCollectionChanged(set, isChecked);
            });

            boolean inCollection = getItem(position).isInCollection();
            holder.myCollectionSwitch.setChecked(inCollection);
        }


        String path = set.getImg_path();
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_bpz_placeholder);

        holder.vImage.setOnClickListener(v -> {
            listener.onSetClicked(set);
        });
        holder.clickableZone.setOnClickListener(v -> {
            listener.onSetClicked(set);
        });
    }

    public CatalogSet getItemAtPosition(int position) {
        return getItem(position);
    }

    void setFilterableList(List<CatalogSet> sets) {
        this.filterableList = sets;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CatalogSet> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(filterableList);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();

                for (CatalogSet set : filterableList) {
                    if (set.getSet().getCode().toLowerCase().contains(pattern)
                            || set.getSet().getName().toLowerCase().contains(pattern)) {
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
            submitList((List<CatalogSet>) filterResults.values);
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vNation;
        ImageView vImage;
        View myCollectionActions;
        View clickableZone;
        SwitchMaterial myCollectionSwitch;

        SetViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txv_set_elem_name);
            vImage = v.findViewById(R.id.imgSet);
            vNation = v.findViewById(R.id.txv_set_elem_nation);
            myCollectionActions = v.findViewById(R.id.my_collection_action);
            clickableZone = v.findViewById(R.id.clickable_zone);
            myCollectionSwitch = v.findViewById(R.id.my_collection_switch);
        }
    }
}
