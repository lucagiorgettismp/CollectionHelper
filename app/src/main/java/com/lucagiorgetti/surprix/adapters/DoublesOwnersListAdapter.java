package com.lucagiorgetti.surprix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.User;

import java.util.ArrayList;

/**
 * Adapter for showing a list of User which owns a selected surprise.
 *
 * Created by Luca on 28/10/2017.
 */

public class DoublesOwnersListAdapter extends BaseAdapter {

    private ArrayList<User> collectors;
    private LayoutInflater inflater;

    public DoublesOwnersListAdapter(Context context, ArrayList<User> collectors) {
        this.collectors = collectors;
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
        v = inflater.inflate(R.layout.doubles_dialog_element, parent, false);

        TextView vUsername = (TextView) v.findViewById(R.id.txv_collector_username);
        TextView vCountry = (TextView) v.findViewById(R.id.txv_collector_country);
        User user = getItem(position);

        vUsername.setText(user.getUsername());
        vCountry.setText(user.getCountry());
        return v;
    }
}
