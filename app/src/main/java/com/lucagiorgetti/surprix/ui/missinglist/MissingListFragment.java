package com.lucagiorgetti.surprix.ui.missinglist;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.MissingDetail;
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.ui.adapters.SurpRecylerAdapterListener;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MissingListFragment extends BaseFragment {
    private MissingRecyclerAdapter mAdapter;
    private SearchView searchView;
    private View root;
    private MissingListViewModel missingListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        missingListViewModel = ViewModelProviders.of(this).get(MissingListViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_missing_list, container, false);
        }
        View emptyList = root.findViewById(R.id.missing_empty_list);
        ProgressBar progress = root.findViewById(R.id.missing_loading);
        RecyclerView recyclerView = root.findViewById(R.id.missing_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MissingRecyclerAdapter(true);

        mAdapter.setListener(new SurpRecylerAdapterListener() {
            @Override
            public void onShowMissingOwnerClick(Surprise surprise) {
                showMissingOwners(surprise);
            }

            @Override
            public void onSaveNotesClick(Surprise surprise, MissingDetail detail) {
                saveNotes(surprise, detail);
            }

            @Override
            public void onDeleteNoteClick(Surprise surp) {
                deleteNotes(surp);
            }
        });
        recyclerView.setAdapter(mAdapter);

        missingListViewModel.getMissingSurprises().observe(this, missingList -> {
            emptyList.setVisibility(missingList == null || missingList.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.submitList(missingList);
            mAdapter.setFilterableList(missingList);
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.missings));
            }
        });

        missingListViewModel.isLoading().observe(this, isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        initSwipe(recyclerView);
        setHasOptionsMenu(true);
        setTitle(getString(R.string.missings));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
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

    private void initSwipe(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                deleteSurprise(mAdapter, position);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void deleteSurprise(MissingRecyclerAdapter mAdapter, int position) {
        MissingSurprise mp = mAdapter.getItemAtPosition(position);
        mAdapter.removeFilterableItem(mp);

        CharSequence query = searchView.getQuery();
        if (query != null && query.length() != 0) {
            mAdapter.getFilter().filter(query);
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        DatabaseUtility.removeMissing(mp.getSurprise().getId());
        if (mAdapter.getItemCount() > 0) {
            setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
        } else {
            setTitle(getString(R.string.missings));
        }
        Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.missing_removed), Snackbar.LENGTH_LONG)
                .setAction(SurprixApplication.getInstance().getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseUtility.addMissing(mp.getSurprise().getId());
                        missingListViewModel.addMissing(mp, position);
                        mAdapter.notifyItemInserted(position);
                        if (mAdapter.getItemCount() > 0) {
                            setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
                        } else {
                            setTitle(getString(R.string.missings));
                        }
                    }
                }).show();
    }

    private void showMissingOwners(final Surprise missing) {
        final DoublesOwnersListAdapter adapter = new DoublesOwnersListAdapter(SurprixApplication.getSurprixContext());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_doubles, null);
        TextView dialogTitle = view.findViewById(R.id.doubles_dialog_title);
        final TextView infoTxv = view.findViewById(R.id.doubles_dialog_info);
        final TextView emptyListTxv = view.findViewById(R.id.doubles_dialog_empty_list);
        dialogTitle.setText(String.format(SurprixApplication.getInstance().getResources().getString(R.string.double_owners_dialog_title), missing.getCode(), missing.getDescription()));
        final ListView listView = view.findViewById(R.id.doubles_dialog_list);

        User currentUser = SurprixApplication.getInstance().getCurrentUser();
        listView.setAdapter(adapter);
        DatabaseUtility.getDoubleOwners(missing.getId(), new FirebaseListCallback<User>() {
            @Override
            public void onSuccess(List<User> users) {
                if (users == null || users.isEmpty()) {
                    emptyListTxv.setVisibility(View.VISIBLE);
                    infoTxv.setVisibility(View.GONE);
                } else {
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
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            public void onStart() {
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void saveNotes(Surprise surprise, MissingDetail detail) {
        DatabaseUtility.addDetailForMissing(surprise.getId(), detail, new FirebaseCallback<Boolean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Boolean item) {
                Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.note_saved), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void deleteNotes(Surprise surprise) {
        MissingDetail detail = new MissingDetail();
        detail.setNotes("");
        DatabaseUtility.addDetailForMissing(surprise.getId(), detail, new FirebaseCallback<Boolean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Boolean item) {
                Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.note_removed), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {

            }
        });
    }


    private void sendEmailToUser(User owner, Surprise missing) {
        User currentUser = SurprixApplication.getInstance().getCurrentUser();
        String to = owner.getEmail().replaceAll(",", "\\.");
        String subject = SurprixApplication.getInstance().getString(R.string.mail_subject, currentUser.getUsername());

        String html = "";
        if (currentUser.getCountry().equals(owner.getCountry())) {
            html = SurprixApplication.getInstance().getString(R.string.mail_exchange_body, currentUser.getUsername(), missing.getCode(), missing.getDescription());
        } else {
            html = SurprixApplication.getInstance().getString(R.string.mail_exchange_body_en, currentUser.getUsername(), missing.getCode(), missing.getDescription());
        }

        Spanned body;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            body = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            body = Html.fromHtml(html);
        }

        sendMail(to, subject, body);
    }

    public void sendMail(String recipient, String subject, Spanned html_body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:" + recipient));
        if (subject != null && !subject.isEmpty()) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (html_body != null && html_body.length() > 0) {
            intent.putExtra(Intent.EXTRA_TEXT, html_body);
        }
        startActivity(Intent.createChooser(intent, "Scegli con quale app inviare una mail:"));
    }
}