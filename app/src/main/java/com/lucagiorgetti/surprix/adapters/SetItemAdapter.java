package com.lucagiorgetti.surprix.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.ArrayList;

/**
 * Adapter for showing a list of Surprises from a selected set.
 *
 * Created by Luca on 28/10/2017.
 */

public class SetItemAdapter extends BaseAdapter {

    private ArrayList<Surprise> items;
    private LayoutInflater inflater;
    private Context ctx;

    public SetItemAdapter(Context context, ArrayList<Surprise> surpList) {
        items = surpList;
        ctx = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Surprise getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View v, final ViewGroup parent) {
        v = inflater.inflate(R.layout.set_detail_element, null);

        View vLayout = v.findViewById(R.id.layout_item_titlebar);
        TextView vCode = v.findViewById(R.id.txv_item_code);
        TextView vDescription = v.findViewById(R.id.txv_item_desc);
        ImageView vImage = v.findViewById(R.id.img_item);
        ImageButton btnAddMissing = v.findViewById(R.id.btn_item_add_missing);
        ImageButton btnAddDouble = v.findViewById(R.id.btn_item_add_double);
        View cardLayout = v.findViewById(R.id.set_detail_layout);

        Surprise s = getItem(position);

        vCode.setText(s.getCode());
        vDescription.setText(s.getDescription());

        String path = s.getImg_path();
        if (path.startsWith("gs")){
            FirebaseStorage storage = SurprixApplication.getInstance().getFirebaseStorage();
            StorageReference gsReference = storage.getReferenceFromUrl(path);
            Glide.with(ctx).
                    load(gsReference).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_surprise_grey))
                    .into(vImage);

        } else {
            Glide.with(ctx).
                    load(s.getImg_path()).
                    apply(new RequestOptions()
                            .placeholder(R.drawable.ic_surprise_grey))
                    .into(vImage);
        }

        vLayout.setBackgroundColor(ContextCompat.getColor(ctx, Colors.getHexColor(s.getSet_producer_color())));
        final GridView gv = (GridView) parent;

        btnAddMissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.performItemClick(v,position,0);
            }
        });

        btnAddDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.performItemClick(v,position,0);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        cardLayout.getLayoutParams().width = (width - 150)/3 ;

        return v;
    }
}
