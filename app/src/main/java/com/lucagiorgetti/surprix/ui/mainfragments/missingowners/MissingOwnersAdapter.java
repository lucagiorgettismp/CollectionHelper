package com.lucagiorgetti.surprix.ui.mainfragments.missingowners;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for showing a list of User which owns a selected surprise.
 * <p>
 * Created by Luca on 28/10/2017.
 */

public class MissingOwnersAdapter extends RecyclerView.Adapter<MissingOwnersAdapter.ViewHolder> {

    private List<User> items = new ArrayList<>();
    private MissingOwnersFragment.MyClickListener listener;

    void setOwners(List<User> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.missing_owners_element, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = items.get(position);
        holder.vUsername.setText(user.getUsername());
        holder.vCountry.setText(user.getCountry());

        holder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOwnerClicked(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vUsername;
        TextView vCountry;
        View vCard;

        ViewHolder(View v) {
            super(v);
            vUsername = v.findViewById(R.id.txv_collector_username);
            vCountry = v.findViewById(R.id.txv_collector_country);
            vCard = v.findViewById(R.id.missing_owner_card);
        }
    }

    public void setListener(MissingOwnersFragment.MyClickListener myClickListener) {
        this.listener = myClickListener;
    }


}
