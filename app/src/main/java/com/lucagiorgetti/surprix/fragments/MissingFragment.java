package com.lucagiorgetti.surprix.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SurpRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.ArrayList;

public class MissingFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, View.OnClickListener{
    private FragmentListenerInterface listener;

    ArrayList<Surprise> missings = new ArrayList<>();
    private SurpRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private View emptyList;
    private Context mContext;
    private ProgressBar progress;
    private SearchView searchView;
    private Paint p = new Paint();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                listener.onClickOpenProducersFragment();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.surprise_fragment, container, false);
        emptyList = layout.findViewById(R.id.empty_list);
        String username;
        username = getArguments().getString("username");
        progress = (ProgressBar) layout.findViewById(R.id.surprise_loading);
        recyclerView = (RecyclerView) layout.findViewById(R.id.surprise_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SurpRecyclerAdapter(mContext, missings);
        recyclerView.setAdapter(mAdapter);
        if (missings != null && !missings.isEmpty()){
            emptyList.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        initSwipe();
        DatabaseUtility.getMissingsForUsername(username, new OnGetListListener<Surprise>() {

            @Override
            public void onSuccess(ArrayList<Surprise> surprises) {
                missings = surprises;

                if(missings != null){
                    emptyList.setVisibility(View.GONE);
                    mAdapter = new SurpRecyclerAdapter(mContext, missings);
                    recyclerView.setAdapter(mAdapter);
                    if(listener != null){
                        listener.setMissingsTitle(missings.size());
                    }
                } else {
                    emptyList.setVisibility(View.VISIBLE);
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onStart() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
                progress.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.data_sync_error, Toast.LENGTH_SHORT).show();
            }
        });

        return layout;
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Surprise s = mAdapter.getItemAtPosition(position);
                if (direction == ItemTouchHelper.LEFT){
                    final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle(getString(R.string.remove_missing_dialog_title));
                    alertDialog.setMessage(getString(R.string.remove_missing_dialog_text) + " " + s.getDescription() + "?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.removeItem(position);
                                    listener.onSwipeRemoveMissing(s.getId());
                                    String queryTxt = searchView.getQuery().toString();
                                    if(!queryTxt.isEmpty()) {
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.detach(MissingFragment.this).attach(MissingFragment.this).commit();
                                        SystemUtility.closeKeyboard(getActivity(), getView());
                                    }

                                    alertDialog.dismiss();
                                    listener.setMissingsTitle(missings.size());
                                    if (missings == null || missings.isEmpty()){
                                        emptyList.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                    alertDialog.show();
                } else {
                    listener.onSwipeShowDoublesOwner(s);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(ContextCompat.getColor(mContext, R.color.SwipeGreen));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(ContextCompat.getColor(mContext, R.color.SwipeRed));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }
        ArrayList<Surprise> filteredValues = new ArrayList<>(missings);
        for (Surprise value : missings) {
            if (!(value.getDescription().toLowerCase().contains(newText.toLowerCase()) ||
                    value.getCode().toLowerCase().contains(newText.toLowerCase()) ||
                    value.getSet_name().toLowerCase().contains(newText.toLowerCase()))) {
                filteredValues.remove(value);
            }
        }
        mAdapter = new SurpRecyclerAdapter(mContext, filteredValues);
        recyclerView.setAdapter(mAdapter);
        return false;
    }

    public void resetSearch() {
        mAdapter = new SurpRecyclerAdapter(mContext, missings);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentListenerInterface){
            listener = (FragmentListenerInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onResume() {
        if(missings != null) {
            listener.setMissingsTitle(missings.size());
        }
        super.onResume();
    }
}

/*
*
try {
File root = new File(Environment.getExternalStorageDirectory(), "Documents");
if (!root.exists()) {
    root.mkdirs();
}
File gpxfile = new File(root, "Mancanti.txt");
FileWriter writer = new FileWriter(gpxfile);

writer.write("Mancanti al 04/12/2017" + String.format("%n") + String.format("%n"));
for(Surprise s : missings){
    writer.write(s.getSet_name() + " -> " + s.getCode() + " - " +s.getDescription() + String.format("%n"));
}
writer.flush();
writer.close();
Toast.makeText(mContext, "Saved" + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
} catch (IOException e) {
e.printStackTrace();
}
*/