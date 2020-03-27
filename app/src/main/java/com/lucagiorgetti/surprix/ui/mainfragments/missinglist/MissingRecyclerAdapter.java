package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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
import com.lucagiorgetti.surprix.model.MissingDetail;
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.adapters.SurpRecylerAdapterListener;

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
    private boolean fromMissing;

    public MissingRecyclerAdapter(boolean fromMissing) {
        super(DIFF_CALLBACK);
        this.fromMissing = fromMissing;
    }

    private static final DiffUtil.ItemCallback<MissingSurprise> DIFF_CALLBACK = new DiffUtil.ItemCallback<MissingSurprise>() {
        @Override
        public boolean areItemsTheSame(@NonNull MissingSurprise oldItem, @NonNull MissingSurprise newItem) {
            MissingDetail oldDetail = oldItem.getDetail();
            MissingDetail newDetail = newItem.getDetail();
            if (oldDetail != null && newDetail != null) {
                return oldItem.getSurprise().getId().equals(newItem.getSurprise().getId()) && oldItem.getDetail().getNotes().equals(newItem.getDetail().getNotes());
            } else {
                return oldItem.getSurprise().getId().equals(newItem.getSurprise().getId());
            }
        }

        @Override
        public boolean areContentsTheSame(@NonNull MissingSurprise oldItem, @NonNull MissingSurprise newItem) {
            MissingDetail oldDetail = oldItem.getDetail();
            MissingDetail newDetail = newItem.getDetail();
            if (oldDetail != null && newDetail != null) {
                return oldItem.getSurprise().getId().equals(newItem.getSurprise().getId()) && oldItem.getDetail().getNotes().equals(newItem.getDetail().getNotes());
            } else {
                return oldItem.getSurprise().getId().equals(newItem.getSurprise().getId());
            }
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
        Surprise surp = getItem(position).getSurprise();
        MissingDetail detail = getItem(position).getDetail();
        holder.vSetName.setText(surp.getSet_name());

        if (surp.isSet_effective_code()) {
            holder.vDescription.setText(surp.getCode() + " - " + surp.getDescription());
        } else {
            holder.vDescription.setText(surp.getDescription());
        }

        holder.vYear.setText(String.valueOf(surp.getSet_year()));
        String productName = surp.getSet_product_name();
        holder.vProducer.setText(surp.getSet_producer_name() + " " + productName);
        if (detail != null) {
            holder.vNotesText.setText(detail.getNotes());
        } else {
            holder.vNotesText.setText(null);
        }
        holder.vNotesText.setOnEditorActionListener((textView, i, keyEvent) -> {

            if (i == EditorInfo.IME_ACTION_DONE) {
                MissingDetail md = new MissingDetail();
                md.setNotes(textView.getText().toString());

                listener.onSaveNotesClick(surp, md);
                holder.vNotesText.clearFocus();
            }

            return false;
        });

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

        holder.vBtnOwners.setOnClickListener(view -> {
            listener.onShowMissingOwnerClick(surp);
        });


        holder.vBtnDeleteNotes.setOnClickListener(view -> {
            holder.vNotesText.setText(null);
            listener.onDeleteNoteClick(surp);
            holder.vNotesText.clearFocus();
        });

        if (!fromMissing) {
            holder.vMissingBottom.setVisibility(View.GONE);
            holder.vBtnOwners.setVisibility(View.GONE);
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
            List<MissingSurprise> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(filterableList);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();

                for (MissingSurprise mp : filterableList) {
                    Surprise surprise = mp.getSurprise();
                    if (surprise.getCode().toLowerCase().contains(pattern)
                            || surprise.getDescription().toLowerCase().contains(pattern)
                            || surprise.getSet_name().toLowerCase().contains(pattern)) {
                        filteredList.add(mp);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            submitList((List<MissingSurprise>) filterResults.values);
        }
    };

    public void setFilterableList(List<MissingSurprise> missingList) {
        this.filterableList = missingList;
    }

    public void removeFilterableItem(MissingSurprise surprise) {
        this.filterableList.remove(surprise);
    }

    public void addFilterableItem(MissingSurprise surprise, int position) {
        this.filterableList.add(position, surprise);
    }

    public void setListener(SurpRecylerAdapterListener listener) {
        this.listener = listener;
    }

    class SurpViewHolder extends RecyclerView.ViewHolder {
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
        ImageButton vBtnOwners;
        ImageButton vBtnDeleteNotes;
        View vMissingBottom;
        EditText vNotesText;

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
            vBtnOwners = v.findViewById(R.id.show_owners_btn);
            vBtnDeleteNotes = v.findViewById(R.id.delete_note_btn);
            vMissingBottom = v.findViewById(R.id.missing_bottom_layout);
            vNotesText = v.findViewById(R.id.note_edit_text);
        }
    }
}
