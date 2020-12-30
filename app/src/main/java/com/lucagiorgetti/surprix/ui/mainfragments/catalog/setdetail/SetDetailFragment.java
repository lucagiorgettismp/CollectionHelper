package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao;
import com.lucagiorgetti.surprix.utility.dao.MissingListDao;

public class SetDetailFragment extends BaseSetDetailFragment {
    MissingListDao missingListDao = new MissingListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());
    DoubleListDao doubleListDao = new DoubleListDao(SurprixApplication.getInstance().getCurrentUser().getUsername());

    @Override
    public void setupView() {
        SetDetailViewModel setDetailViewModel = new ViewModelProvider(this).get(SetDetailViewModel.class);

        mAdapter.setListener(new MyClickListener() {
            @Override
            public void onSurpriseAddedToDoubles(Surprise s) {
                doubleListDao.addDouble(s.getId());
                Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.add_to_doubles) + ": " + s.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> doubleListDao.removeDouble(s.getId())).show();
            }

            @Override
            public void onSurpriseAddedToMissings(Surprise s) {
                missingListDao.addMissing(s.getId());
                Snackbar.make(getView(), SurprixApplication.getInstance().getString(R.string.added_to_missings) + ": " + s.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction(SurprixApplication.getInstance().getString(R.string.undo), view -> missingListDao.removeMissing(s.getId())).show();
            }
        });

        recyclerView.setAdapter(mAdapter);

        String setId = null;
        if (getArguments() != null) {
            setId = getArguments().getString("set_id");
            String setName = getArguments().getString("set_name");
            setTitle(setName);
        }

        setDetailViewModel.getSurprises(setId, CatalogNavigationMode.CATALOG).observe(getViewLifecycleOwner(), surprises -> {
            mAdapter.setSurprises(surprises);
            mAdapter.notifyDataSetChanged();
        });

        setDetailViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

    }

    public interface MyClickListener {
        void onSurpriseAddedToDoubles(Surprise s);

        void onSurpriseAddedToMissings(Surprise s);
    }
}