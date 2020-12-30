package com.lucagiorgetti.surprix.ui.mainfragments.aboutme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.ui.activities.LoginActivity;
import com.lucagiorgetti.surprix.ui.activities.SettingsActivity;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers.CatalogProducerFragmentDirections;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class AboutMeFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        User user = SurprixApplication.getInstance().getCurrentUser();

        View root = inflater.inflate(R.layout.fragment_about_me, container, false);

        View collectionBtn = root.findViewById(R.id.my_collection_btn);
        Button logout = root.findViewById(R.id.logout_btn);

        TextView username = root.findViewById(R.id.username);
        TextView email = root.findViewById(R.id.email);

        username.setText(user.getUsername());
        email.setText(user.getEmail());

        collectionBtn.setOnClickListener(view -> {
            NavDirections action = AboutMeFragmentDirections.actionNavigationAboutMeToNavigationMyCollection();
            Navigation.findNavController(view).navigate(action);
        });

        logout.setOnClickListener(v -> {
            SystemUtils.logout();
            SystemUtils.openNewActivityWithFinishing(getActivity(), LoginActivity.class);
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_collection_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            SystemUtils.openNewActivity(SettingsActivity.class, null);
        }
        return super.onOptionsItemSelected(item);
    }
}