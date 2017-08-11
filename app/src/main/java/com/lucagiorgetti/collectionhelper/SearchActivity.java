package com.lucagiorgetti.collectionhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public static ListView setsListView;
    public static SearchView setsSearchView;
    public static ArrayList<Set> setsList;
    private static DbManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sets);
        setsListView = (ListView)findViewById(R.id.set_list);
        setsSearchView = (SearchView) findViewById(R.id.set_search);
        manager = new DbManager(this);
        setsList = manager.getSets();

        final ArrayAdapter adapt = new SetAdapter(this, R.layout.sets_element, setsList, manager);

        setsListView.setAdapter(adapt);
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
}
