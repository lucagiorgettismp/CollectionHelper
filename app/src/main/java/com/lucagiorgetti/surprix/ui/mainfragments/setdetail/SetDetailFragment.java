package com.lucagiorgetti.surprix.ui.mainfragments.setdetail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;

public class SetDetailFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SetDetailViewModel setDetailViewModel = new ViewModelProvider(this).get(SetDetailViewModel.class);

        View root = inflater.inflate(R.layout.fragment_set_detail, container, false);

        SetDetailRecyclerAdapter mAdapter;

        ProgressBar progress = root.findViewById(R.id.set_detail_loading);
        RecyclerView recyclerView = root.findViewById(R.id.set_detail_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SetDetailRecyclerAdapter();
        mAdapter.setListener(s -> {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(getString(R.string.add_surprise_dialog_title));
            alertDialog.setMessage(getString(R.string.add_surprise_dialog_message) + " " + s.getDescription() + "?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.missings),
                    (dialog, which) -> {
                        DatabaseUtils.addMissing(s.getId());
                        Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.added_to_missings) + ": " + s.getDescription(), Snackbar.LENGTH_LONG)
                                .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> DatabaseUtils.removeMissing(s.getId())).show();
                        alertDialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.doubles),
                    (dialog, which) -> {
                        DatabaseUtils.addDouble(s.getId());
                        alertDialog.dismiss();
                        Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.add_to_doubles) + ": " + s.getDescription(), Snackbar.LENGTH_LONG)
                                .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> DatabaseUtils.removeDouble(s.getId())).show();
                    });
            alertDialog.show();
        });

        recyclerView.setAdapter(mAdapter);

        String setId = null;
        if (getArguments() != null) {
            setId = getArguments().getString("set_id");
            String setName = getArguments().getString("set_name");
            setTitle(setName);
        }

        setDetailViewModel.getSurprises(setId).observe(getViewLifecycleOwner(), surprises -> {
            mAdapter.setSurprises(surprises);
            mAdapter.notifyDataSetChanged();
        });

        setDetailViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface MyClickListener {
        void onSurpriseAdded(Surprise s);
    }
}