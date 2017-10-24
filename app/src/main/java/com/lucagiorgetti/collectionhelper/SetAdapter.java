package com.lucagiorgetti.collectionhelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Set;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Utente on 07/04/2017.
 */

public class SetAdapter extends ArrayAdapter<Set> implements Filterable {
    private final Context context;
    ArrayList<Set> mData;
    ArrayList<Set> mStringFilterList;
    private final int layoutResourceId;
    private ValueFilter valueFilter;

    public SetAdapter(Context context, int layoutResourceId, ArrayList<Set> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.mData = data;
        this.mStringFilterList = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Set getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textViewSetName = (TextView)row.findViewById(R.id.txv_setname);
            holder.textViewSeason = (TextView)row.findViewById(R.id.txv_season);
            holder.textViewProducer = (TextView)row.findViewById(R.id.txv_producer);
            holder.textViewYear = (TextView)row.findViewById(R.id.txv_setyear);
            holder.imgView = (ImageView)row.findViewById(R.id.imgSet);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Set set = mData.get(position);

        holder.textViewSetName.setText(set.getName());
        holder.textViewSeason.setText(set.getSeason());
        holder.textViewProducer.setText(set.getProducer().getName());
        holder.textViewYear.setText(String.valueOf(set.getYear()));

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(set.getImg_path());
        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                // ...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });

        if(localFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
            holder.imgView.setImageBitmap(myBitmap);
        }

        if(position %2 == 1)
        {
            row.setBackgroundResource(R.color.background_pari);
        }
        else
        {
            row.setBackgroundResource(R.color.background_dispari);
        }
        return row;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (valueFilter == null){
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List filterList = new ArrayList();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder
    {
        TextView textViewYear;
        TextView textViewSeason;
        TextView textViewSetName;
        TextView textViewProducer;
        ImageView imgView;
    }
}