package com.lucagiorgetti.collectionhelper;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity{
    public static ListView setsListView;
    public static SearchView setsSearchView;
    public static ArrayList<Set> setsList = new ArrayList<>();
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    public SetAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sets);
        setsListView = (ListView)findViewById(R.id.set_list);
        setsSearchView = (SearchView) findViewById(R.id.set_search);

        adapt = new SetAdapter(this, R.layout.sets_element, setsList);
        setsListView.setAdapter(adapt);
        getDataFromServer();
        /*setsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Set setClicked = (Set) setsListView.getItemAtPosition(position);
                    Log.w("SETS", "Cliccato: " + setClicked.getName());
                    ArrayList<Surprise> surprises = manager.getSurprisesBySetId(setClicked);
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View inflatedView = layoutInflater.inflate(R.layout.popup_surprises, null, false);
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    ListView popupListView = (ListView) inflatedView.findViewById(R.id.surprise_list);
                    SurpriseAdapter surpAdap = new SurpriseAdapter(SearchActivity.this, R.layout.list_element, surprises, manager);
                    popupListView.setAdapter(surpAdap);
                    PopupWindow popWindow = new PopupWindow(inflatedView, size.x - 50, size.y - 500, true);
                    popWindow.setFocusable(true);
                    popWindow.setOutsideTouchable(true);
                    popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 150);
                }
            });
            */
        setsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapt.getFilter().filter(newText);
                return false;
            }
        });

    }

    public void getDataFromServer(){
        setsList.clear();
        dbRef.child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Set s = d.getValue(Set.class);
                        setsList.add(s);
                        adapt.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setsListView.setAdapter(adapt);
    }
}
