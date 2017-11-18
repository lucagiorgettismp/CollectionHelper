package com.lucagiorgetti.collectionhelper.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.model.Colors;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

/**
 * Created by Luca on 28/10/2017.
 */

public class SetItemAdapter extends BaseAdapter {

    private ArrayList<Surprise> items;
    private LayoutInflater inflater;
    Context ctx;

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
        v = inflater.inflate(R.layout.set_items_element, null);

        View vLayout = (View) v.findViewById(R.id.layout_item_titlebar);
        TextView vCode = (TextView) v.findViewById(R.id.txv_item_code);
        TextView vDescription = (TextView) v.findViewById(R.id.txv_item_desc);
        ImageView vImage = (ImageView) v.findViewById(R.id.img_item);
        ImageButton btnAddMissing = (ImageButton) v.findViewById(R.id.btn_item_add_missing);
        ImageButton btnAddDouble = (ImageButton) v.findViewById(R.id.btn_item_add_double);

        Surprise s = getItem(position);

        vCode.setText(s.getCode());
        vDescription.setText(s.getDescription());
        Glide.with(ctx).load(s.getImg_path()).into(vImage);

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

        return v;
    };
}
