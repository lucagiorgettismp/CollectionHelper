package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.model.Year;

import java.util.ArrayList;
import java.util.List;

public class ProducerDao {
    private static DatabaseReference producers = SurprixApplication.getInstance().getDatabaseReference().child("producers");
    private static YearDao yearDao = new YearDao();

    public static void getProducerYears(String producerId, final FirebaseListCallback<Year> listen) {
        listen.onStart();

        final ArrayList<Year> years = new ArrayList<>();

        producers.child(producerId).child("years").orderByChild("year").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        yearDao.getYearById(d.getKey(), new CallbackInterface<Year>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Year item) {
                                years.add(item);
                                listen.onSuccess(years);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                } else {
                    listen.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static void getProducers(final FirebaseListCallback<Producer> listen) {
        listen.onStart();

        final List<Producer> prods = new ArrayList<>();

        producers.orderByChild("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Producer p = d.getValue(Producer.class);
                        prods.add(p);
                        listen.onSuccess(prods);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void getProducerById(String producerId, CallbackInterface<Producer> listen){
        producers.child(producerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Producer producer = snapshot.getValue(Producer.class);
                    listen.onSuccess(producer);
                }
                listen.onFailure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
