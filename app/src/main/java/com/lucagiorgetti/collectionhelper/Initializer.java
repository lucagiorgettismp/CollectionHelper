package com.lucagiorgetti.collectionhelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lucagiorgetti.collectionhelper.model.Colors;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.Product;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;
import com.lucagiorgetti.collectionhelper.model.User;

import java.util.Locale;

/**
 * Created by Luca on 06/11/2017.
 */

public class Initializer {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference surprises = database.getReference("surprises");
    DatabaseReference sets = database.getReference("sets");
    DatabaseReference missings = database.getReference("missings");
    User user = null;

    public Initializer(User currentUser){
        this.user = currentUser;
    }

    public void insertData(){
        Producer kinder = new Producer("Kinder");
        Product kinder_sorpresa = new Product("Sorpresa", kinder);
        Product kinder_merendero = new Product("Merendero", kinder);

        Set puffi3 = new Set("Puffi 3", 2016, kinder_sorpresa, Locale.ITALIAN.toString(), "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/BPZ_Puffi_2016.jpg?alt=media&token=2ca97512-463a-4d96-a1d8-6a72232d1317", Colors.LIGHT_BLUE);
        insertSet(puffi3);

        Surprise SD324 = new Surprise("Puffetta", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/SD324.jpg?alt=media&token=2fa2a745-4079-4ae3-8482-4b7272fa207a", "SD324", puffi3);
        insertSurprise(SD324);

        Surprise SD325 = new Surprise("Gargamella", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/SD325.jpg?alt=media&token=fb1ec363-7edd-48c4-9414-f98d085838aa", "SD325", puffi3);
        insertSurprise(SD325);

        insertMissing(SD324);
        insertMissing(SD325);
    }

    private void insertMissing(Surprise surp) {
        missings.child(user.getUsername()).child(surp.getId()).setValue(true);
    }

    private void insertSurprise(Surprise surp){
        surprises.child(surp.getId()).setValue(surp);
        sets.child(surp.getSet_id()).child("surprises").child(surp.getId()).setValue(true);
    }

    private void insertSet(Set set){
        sets.child(set.getId()).setValue(set);
    }
}
