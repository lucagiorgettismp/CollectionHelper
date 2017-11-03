package com.lucagiorgetti.collectionhelper.Db;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;
import com.lucagiorgetti.collectionhelper.model.User;

import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private Context context;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public DbManager(Context context){
        this.context = context;
    }

    public User getUserByEmail(String email) {
        DatabaseReference ref = db.getReference("users");
        ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                System.out.println(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return null;
    }

    public ArrayList<Surprise> getMissings(String username) {
        // TODO: to be implemented
        return null;
    }

    public void removeMissing(Surprise surprise) {
        // TODO: to be implemented
        return;
    }


    public ArrayList<Set> getSets() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayList<Set> sets = new ArrayList<Set>();
        database.child("sets").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    Set set = noteDataSnapshot.getValue(Set.class);
                    sets.add(set);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return sets;
    }


    public void deleteMissing(Surprise itemClicked) {
    }

    public ArrayList<Surprise> getSurprisesBySetId(Set setClicked) {
        // TODO: to be implemented
        return null;
    }
}
