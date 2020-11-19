package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

import android.annotation.SuppressLint;
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
import com.google.android.material.chip.Chip;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.ExtraLocales;
import com.lucagiorgetti.surprix.model.Missing;
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.missinglist.filter.FilterSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for showing a list of Surprises.
 * <p>
 * Created by Luca on 28/10/2017.
 */

public class MissingRecyclerAdapter extends ListAdapter<MissingSurprise, MissingRecyclerAdapter.SurpViewHolder> implements Filterable {
    private SurpRecylerAdapterListener listener;
    private List<MissingSurprise> filterableList;
    List<MissingSurprise> textFilteredValues = new ArrayList<>();
    private FilterSelection selectionFilter;

    MissingRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<MissingSurprise> DIFF_CALLBACK = new DiffUtil.ItemCallback<MissingSurprise>() {
        @Override
        public boolean areItemsTheSame(@NonNull MissingSurprise oldItem, @NonNull MissingSurprise newItem) {
            return oldItem.getSurprise().getId().equals(newItem.getSurprise().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MissingSurprise oldItem, @NonNull MissingSurprise newItem) {
            return oldItem.getSurprise().getId().equals(newItem.getSurprise().getId());
        }
    };

    @NonNull
    @Override
    public SurpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_surprise_list, parent, false);
        return new SurpViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SurpViewHolder holder, int position) {
        MissingSurprise ms = getItem(position);
        Surprise surp = ms.getSurprise();
        holder.vSetName.setText(surp.getSet_name());

        if (surp.isSet_effective_code()) {
            holder.vDescription.setText(surp.getCode() + " - " + surp.getDescription());
        } else {
            holder.vDescription.setText(surp.getDescription());
        }

        holder.vYear.setText(surp.getSet_year_name());
        holder.vProducer.setText(surp.getSet_producer_name());

        String nation;
        if (ExtraLocales.isExtraLocale(surp.getSet_nation())) {
            nation = ExtraLocales.getDisplayName(surp.getSet_nation());
        } else {
            Locale l = new Locale("", surp.getSet_nation());
            nation = l.getDisplayCountry();
        }

        holder.vNation.setText(nation);

        holder.vLayout.setBackgroundColor(ContextCompat.getColor(SurprixApplication.getSurprixContext(), Colors.getHexColor(surp.getSet_producer_color())));

        String path = surp.getImg_path();
        if (path.startsWith("gs")) {
            FirebaseStorage storage = SurprixApplication.getInstance().getFirebaseStorage();
            StorageReference gsReference = storage.getReferenceFromUrl(path);
            Glide.with(SurprixApplication.getSurprixContext()).
                    load(gsReference).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_logo_shape_primary))
                    .into(holder.vImage);

        } else {
            Glide.with(SurprixApplication.getSurprixContext()).
                    load(path).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_logo_shape_primary))
                    .into(holder.vImage);
        }

        holder.vBtnOwners.setVisibility(View.VISIBLE);
        holder.vBtnOwners.setOnClickListener(view -> listener.onShowMissingOwnerClick(surp));

        holder.delete.setOnClickListener(v -> listener.onSurpriseDelete(position));

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

    public MissingSurprise getItemAtPosition(int position) {
        return getItem(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<MissingSurprise> pippo = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                pippo.addAll(filterableList);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (MissingSurprise missingSurprise : filterableList) {
                    Surprise surprise = missingSurprise.getSurprise();
                    if (surprise.getCode().toLowerCase().contains(pattern)
                            || surprise.getDescription().toLowerCase().contains(pattern)){
                            //|| surprise.getSet_name().toLowerCase().contains(pattern)) {
                        pippo.add(missingSurprise);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = applyFilter(pippo);

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            textFilteredValues = (List<MissingSurprise>) filterResults.values;
            submitList(textFilteredValues);
        }
    };

    void setFilterableList(List<MissingSurprise> missingList) {
        this.filterableList = missingList;
        this.textFilteredValues = missingList;
    }

    void removeFilterableItem(MissingSurprise surprise) {
        this.filterableList.remove(surprise);
    }

    void addFilterableItem(MissingSurprise surprise, int position) {
        this.filterableList.add(position, surprise);
    }

    void setListener(SurpRecylerAdapterListener listener) {
        this.listener = listener;
    }

    public void setFilter(FilterSelection selection) {
        this.selectionFilter = selection;
        submitList(applyFilter(textFilteredValues));
    }

    private List<MissingSurprise> applyFilter(List<MissingSurprise> values) {
        if (selectionFilter == null){
            return values;
        } else {
            List<MissingSurprise> returnList = new ArrayList<>();
            for (MissingSurprise missingSurprise : values) {
                Surprise s = missingSurprise.getSurprise();

                if (selectionFilter.getCategories().contains(s.getSet_category())
                        && selectionFilter.getYears().contains(s.getSet_year_name())
                        && selectionFilter.getProducers().contains(s.getSet_producer_name())) {
                    returnList.add(missingSurprise);
                }
            }
            return returnList;
        }
    }

    public void removeFilter() {
        this.selectionFilter = null;
        submitList(textFilteredValues);
    }

    static class SurpViewHolder extends RecyclerView.ViewHolder {
        TextView vSetName;
        TextView vDescription;
        TextView vYear;
        TextView vProducer;
        TextView vNation;
        TextView delete;
        ImageView vImage;
        ImageView vStar1On;
        ImageView vStar2On;
        ImageView vStar3On;
        ImageView vStar1Off;
        ImageView vStar2Off;
        ImageView vStar3Off;
        Chip vBtnOwners;
        View vLayout;

        SurpViewHolder(View v) {
            super(v);
            vSetName = v.findViewById(R.id.txv_surp_elem_set);
            vDescription = v.findViewById(R.id.txv_surp_elem_desc);
            vYear = v.findViewById(R.id.txv_surp_elem_year);
            vProducer = v.findViewById(R.id.txv_surp_elem_producer);
            vNation = v.findViewById(R.id.txv_surp_elem_nation);
            vImage = v.findViewById(R.id.img_surp_elem);
            vLayout = v.findViewById(R.id.layout_surp_elem_titlebar);
            vStar1On = v.findViewById(R.id.img_surp_elem_star_1_on);
            vStar2On = v.findViewById(R.id.img_surp_elem_star_2_on);
            vStar3On = v.findViewById(R.id.img_surp_elem_star_3_on);
            vStar1Off = v.findViewById(R.id.img_surp_elem_star_1_off);
            vStar2Off = v.findViewById(R.id.img_surp_elem_star_2_off);
            vStar3Off = v.findViewById(R.id.img_surp_elem_star_3_off);
            vBtnOwners = v.findViewById(R.id.show_owners_chip);
            delete = v.findViewById(R.id.delete);
        }
    }
}
