package com.lucagiorgetti.collectionhelper.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.lucagiorgetti.collectionhelper.model.User;

import java.util.ArrayList;

/**
 * Created by Luca on 28/10/2017.
 */

public class DoublesOwnersListAdapter extends BaseAdapter {

    private ArrayList<User> collectors;
    private LayoutInflater inflater;
    Context ctx;

    public DoublesOwnersListAdapter(Context context, ArrayList<User> collectors) {
        this.collectors = collectors;
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return collectors.size();
    }

    @Override
    public User getItem(int position) {
        return collectors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View v, final ViewGroup parent) {
        v = inflater.inflate(R.layout.doubles_dialog_element, null);

        TextView vUsername = (TextView) v.findViewById(R.id.txv_collector_username);
        User user = getItem(position);

        vUsername.setText(user.getUsername());
        return v;
    };
}
