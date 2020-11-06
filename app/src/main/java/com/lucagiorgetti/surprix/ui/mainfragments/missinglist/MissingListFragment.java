package com.lucagiorgetti.surprix.ui.mainfragments.missinglist;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.model.MissingDetail;
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.Common;
import com.lucagiorgetti.surprix.utility.PDFUtils;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MissingListFragment extends BaseFragment {
    private MissingRecyclerAdapter mAdapter;
    private SearchView searchView;
    private View root;
    private View emptyList;
    private List<MissingSurprise> missingSurprises = new ArrayList<>();
    private MissingListDao missingListDao = new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MissingListViewModel missingListViewModel = new ViewModelProvider(this).get(MissingListViewModel.class);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_missing_list, container, false);
        }
        emptyList = root.findViewById(R.id.missing_empty_list);
        ProgressBar progress = root.findViewById(R.id.missing_loading);
        RecyclerView recyclerView = root.findViewById(R.id.missing_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = root.findViewById(R.id.missing_fab);
        fab.setOnClickListener(view -> Dexter.withActivity(getActivity()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                try {
                    PDFUtils.createMissingListPdfFile(getActivity(), missingSurprises, Common.getAppPath(SurprixApplication.getSurprixContext()) + getResources().getString(R.string.pdf_file_name) + ".pdf");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check());

        mAdapter = new MissingRecyclerAdapter();

        mAdapter.setListener(new SurpRecylerAdapterListener() {
            @Override
            public void onShowMissingOwnerClick(Surprise surprise) {
                Navigation.findNavController(root).navigate(MissingListFragmentDirections.actionNavigationMissingListToNavigationMissingOwners(surprise.getId()));
            }

            @Override
            public void onSaveNotesClick(Surprise surprise, MissingDetail detail) {
                saveNotes(surprise, detail);
                missingListViewModel.loadMissingSurprises();
            }

            @Override
            public void onDeleteNoteClick(Surprise surp) {
                deleteNotes(surp);
                missingListViewModel.loadMissingSurprises();
            }

            @Override
            public void onSurpriseDelete(int position) {
                deleteSurprise(mAdapter, position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy != 0) {
                    fab.hide();
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (missingSurprises != null && !missingSurprises.isEmpty()) {
                        fab.show();
                    }
                }
            }
        });

        missingListViewModel.getMissingSurprises().observe(getViewLifecycleOwner(), missingList -> {
            emptyList.setVisibility(missingList == null || missingList.isEmpty() ? View.VISIBLE : View.GONE);
            missingSurprises = missingList;
            mAdapter.submitList(missingList);
            mAdapter.setFilterableList(missingList);
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.missings));
                fab.setVisibility(View.GONE);
            }
        });

        missingListViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

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
        searchView.setOnCloseListener(() -> {
            if (mAdapter.getItemCount() > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
            } else {
                setTitle(getString(R.string.missings));
            }
            return false;
        });
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
        CharSequence query = searchView.getQuery();
        missingListDao.removeMissing(mp.getSurprise().getId());
        mAdapter.removeFilterableItem(mp);
        if (query != null && query.length() != 0) {
            mAdapter.getFilter().filter(query);
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        if (mAdapter.getItemCount() > 0) {
            emptyList.setVisibility(View.GONE);
            setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
        } else {
            emptyList.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.missings));
        }

        if (query != null && query.length() != 0) {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.missing_removed), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.missing_removed), Snackbar.LENGTH_LONG)
                    .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> {
                        missingListDao.addMissing(mp.getSurprise().getId());
                        mAdapter.addFilterableItem(mp, position);
                        mAdapter.notifyItemInserted(position);
                        if (mAdapter.getItemCount() > 0) {
                            emptyList.setVisibility(View.GONE);
                            setTitle(getString(R.string.missings) + " (" + mAdapter.getItemCount() + ")");
                        } else {
                            emptyList.setVisibility(View.VISIBLE);
                            setTitle(getString(R.string.missings));
                        }
                    }).show();
        }
    }

    private void saveNotes(Surprise surprise, MissingDetail detail) {
        missingListDao.addDetailForMissing(surprise.getId(), detail, new CallbackInterface<Boolean>() {
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
        missingListDao.addDetailForMissing(surprise.getId(), detail, new CallbackInterface<Boolean>() {
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
}