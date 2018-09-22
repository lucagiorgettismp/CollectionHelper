package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.ArrayList;

/**
 * Adapter for showing a list of Surprises from a selected set.
 *
 * Created by Luca on 28/10/2017.
 */

public class SetDetailRecyclerAdapter extends RecyclerView.Adapter<SetDetailRecyclerAdapter.SetDetailViewHolder> {

    private ArrayList<Surprise> items;
    private Context ctx;

    public SetDetailRecyclerAdapter(Context context, ArrayList<Surprise> surpList) {
        items = surpList;
        ctx = context;
    }

    public Surprise getItemAtPosition(int position) {
        return this.items.get(position);
    }

    @NonNull
    @Override
    public SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_detail_element, parent, false);

        return new SetDetailViewHolder(v, new SetDetailViewHolder.MyClickListener() {
            @Override
            public void onAddMissingClick(int p) {
                Surprise s = getItemAtPosition(p);
                DatabaseUtility.addMissing(s.getId());
                Snackbar.make(v, SurprixApplication.getInstance().getString(R.string.added_to_missings)+ ": " + s.getDescription() , Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onAddDoubleClick(int p) {
                Surprise s = getItemAtPosition(p);
                DatabaseUtility.addDouble(s.getId());
                Snackbar.make(v, SurprixApplication.getInstance().getString(R.string.added_to_doubles) + ": " + s.getDescription() , Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailViewHolder holder, int position) {
        Surprise s = items.get(position);
        holder.vCode.setText(s.getCode());
        holder.vDescription.setText(s.getDescription());
        holder.titleBar.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(s.getSet_producer_color())));

        String path = s.getImg_path();
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
                    load(s.getImg_path()).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_surprise_grey))
                    .into(holder.vImage);
        }
    }

    @Override
    public int getItemCount() {
        if (items == null){
            return 0;
        }
        return items.size();
    }

    static class SetDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View vLayout;
        TextView vCode;
        TextView vDescription;
        ImageView vImage;
        ImageButton btnAddMissing;
        ImageButton btnAddDouble;
        View cardLayout;
        View titleBar;
        MyClickListener listner;

        SetDetailViewHolder(View v, MyClickListener listener) {
            super(v);

            this.listner = listener;
            vLayout = v.findViewById(R.id.layout_item_titlebar);
            vCode = v.findViewById(R.id.txv_item_code);
            vDescription = v.findViewById(R.id.txv_item_desc);
            vImage = v.findViewById(R.id.img_item);
            btnAddMissing = v.findViewById(R.id.btn_item_add_missing);
            btnAddDouble = v.findViewById(R.id.btn_item_add_double);
            cardLayout = v.findViewById(R.id.set_detail_layout);
            titleBar = v.findViewById(R.id.layout_item_titlebar);
            btnAddDouble.setOnClickListener(this);
            btnAddMissing.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
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
