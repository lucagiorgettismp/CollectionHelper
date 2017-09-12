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

import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public static ListView setsListView;
    public static SearchView setsSearchView;
    public static ArrayList<Set> setsList;
    private static DbManager manager;
    public SetAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sets);
        setsListView = (ListView)findViewById(R.id.set_list);
        setsSearchView = (SearchView) findViewById(R.id.set_search);
        manager = new DbManager(this);
        setsList = manager.getSets();

        adapt = new SetAdapter(this, R.layout.sets_element, setsList, manager);

        setsListView.setAdapter(adapt);
        setsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Set setClicked = (Set) setsListView.getItemAtPosition(position);
                Log.w("SETS","Cliccato: " + setClicked.getName());
                ArrayList<Surprise> surprises = manager.getSurprisesBySetId(setClicked.getSetId());
                LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View inflatedView = layoutInflater.inflate(R.layout.popup_surprises, null,false);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                ListView popupListView = (ListView)inflatedView.findViewById(R.id.list);
                SurpriseAdapter surpAdap = new SurpriseAdapter(SearchActivity.this, R.layout.list_element, surprises, manager);
                popupListView.setAdapter(surpAdap);
                PopupWindow popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 500, true );
                popWindow.setFocusable(true);
                popWindow.setOutsideTouchable(true);
                popWindow.showAtLocation(view, Gravity.BOTTOM, 0,150);
            }
        });
        setsSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        adapt.filter(text);
        return false;
    }
}
