package com.lucagiorgetti.surprix.ui.missinglist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.adapters.DoublesOwnersListAdapter;
import com.lucagiorgetti.surprix.adapters.SurpRecyclerAdapter;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MissingListFragment extends Fragment {
    private MissingListViewModel missingListViewModel;
    private SurpRecyclerAdapter mAdapter;
    SearchView searchView;
    Paint p = new Paint();
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        missingListViewModel =
                ViewModelProviders.of(this).get(MissingListViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_missing_list, container, false);
        }
        View emptyList = root.findViewById(R.id.missing_empty_list);
        ProgressBar progress = root.findViewById(R.id.missing_loading);
        RecyclerView recyclerView = root.findViewById(R.id.missing_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SurpRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        missingListViewModel.getMissingSurprises().observe(this, missingList -> {
            emptyList.setVisibility(missingList == null || missingList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(missingList);
            mAdapter.setFilterableList(missingList);
        });

        missingListViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        initSwipe(recyclerView);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSwipe( RecyclerView recyclerView) {
        final SurpRecyclerAdapter mAdapter = (SurpRecyclerAdapter) recyclerView.getAdapter();
        Context mContext = getContext();

            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    final Surprise s = mAdapter.getItemAtPosition(position);
                    if (direction == ItemTouchHelper.LEFT) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle(getString(R.string.remove_missing_dialog_title));
                        alertDialog.setMessage(getString(R.string.remove_missing_dialog_text) + " " + s.getDescription() + "?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                                (dialog, which) -> {
                                    deleteSurprise(mAdapter, position);
                                    alertDialog.dismiss();
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                                (dialog, which) -> {
                                    alertDialog.dismiss();
                                    mAdapter.notifyDataSetChanged();
                                });
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    } else {
                        onSwipeShowDoublesOwner(s);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                private void onSwipeShowDoublesOwner(final Surprise missing) {
                    final DoublesOwnersListAdapter adapter = new DoublesOwnersListAdapter(mContext);
                    final View view = getLayoutInflater().inflate(R.layout.dialog_doubles, null);
                    TextView dialogTitle = view.findViewById(R.id.doubles_dialog_title);
                    final TextView infoTxv = view.findViewById(R.id.doubles_dialog_info);
                    final TextView emptyListTxv = view.findViewById(R.id.doubles_dialog_empty_list);
                    dialogTitle.setBackgroundColor(ContextCompat.getColor(mContext, Colors.getHexColor(missing.getSet_producer_color())));
                    dialogTitle.setText(String.format(getString(R.string.double_owners_dialog_title), missing.getCode(), missing.getDescription()));
                    final ListView listView = view.findViewById(R.id.doubles_dialog_list);

                    User currentUser = SurprixApplication.getInstance().getCurrentUser();
                    listView.setAdapter(adapter);
                    DatabaseUtility.getDoubleOwners(missing.getId(), new FirebaseListCallback<User>() {
                        @Override
                        public void onSuccess(List<User> users) {
                            if (users != null) {
                                if (!users.isEmpty()) {
                                    final ArrayList<User> owners = new ArrayList<>();
                                    final ArrayList<User> abroad_owners = new ArrayList<>();
                                    for (User u : users) {
                                        if (Objects.equals(u.getCountry(), currentUser.getCountry())) {
                                            owners.add(u);
                                        } else {
                                            abroad_owners.add(u);
                                        }
                                    }

                                    owners.addAll(abroad_owners);

                                    adapter.addOwners(owners);
                                    adapter.notifyDataSetChanged();
                                    emptyListTxv.setVisibility(View.GONE);
                                    infoTxv.setVisibility(View.VISIBLE);
                                    if (owners.size() > 0){
                                        infoTxv.setVisibility(View.GONE);
                                        emptyListTxv.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onFailure() {

                        }
                    });


                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(view);
                    final AlertDialog alert = builder.create();
                    alert.show();

                    listView.setOnItemClickListener((parent, v, position, id) -> {
                        alert.dismiss();
                        final User owner = adapter.getItem(position);
                        sendEmailToUser(owner, missing);
                    });
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        if (dX > 0) {
                            p.setColor(ContextCompat.getColor(mContext, R.color.SwipeGreen));
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        } else {
                            p.setColor(ContextCompat.getColor(mContext, R.color.SwipeRed));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void sendEmailToUser(User owner, Surprise missing) {
        User currentUser = SurprixApplication.getInstance().getCurrentUser();
        String to = owner.getEmail().replaceAll(",", "\\.");
        String subject = getString(R.string.mail_subject, currentUser.getUsername());
        String html = getString(R.string.mail_exchange_body, currentUser.getUsername(), missing.getCode(), missing.getDescription());
        Spanned body;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            body = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            body = Html.fromHtml(html);
        }

        SystemUtility.sendMail(getContext(), to, subject, body);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void deleteSurprise(SurpRecyclerAdapter mAdapter, int position) {
        Surprise surprise = mAdapter.getItemAtPosition(position);
        mAdapter.removeFilterableItem(surprise);

        CharSequence query = searchView.getQuery();
        if ( query != null && query.length() != 0 ){
            mAdapter.getFilter().filter(query);
        } else  {
            mAdapter.notifyItemRemoved(position);
        }
        DatabaseUtility.removeMissing(surprise.getId());
    }
}