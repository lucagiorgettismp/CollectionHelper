package com.lucagiorgetti.surprix.ui.mainfragments.missingowners;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.BaseFragment;

public class MissingOwnersFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_missing_owners, container, false);
        View emptyList = root.findViewById(R.id.missing_owner_empty_list);
        RecyclerView recyclerView = root.findViewById(R.id.missing_owner_recycler);
        ProgressBar progress = root.findViewById(R.id.missing_owner_loading);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MissingOwnersAdapter adapter = new MissingOwnersAdapter();
        adapter.setListener(new MyClickListener(root));

        recyclerView.setAdapter(adapter);

        MissingOwnersViewModel mViewModel = new ViewModelProvider(this).get(MissingOwnersViewModel.class);
        String surpriseId = MissingOwnersFragmentArgs.fromBundle(requireArguments()).getMissingSurpriseId();
        mViewModel.getMissingOwners(surpriseId).observe(getViewLifecycleOwner(), owners -> {
            if (!owners.isEmpty()) {
                emptyList.setVisibility(View.GONE);
                adapter.setOwners(owners);
                adapter.notifyDataSetChanged();
            } else {
                emptyList.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        setTitle(getString(R.string.find_surprise));
        return root;
    }

    static class MyClickListener {
        View root;

        MyClickListener(View v) {
            root = v;
        }

        void onOwnerClicked(User user) {
            Navigation.findNavController(root).navigate(MissingOwnersFragmentDirections.actionNavigationMissingOwnersToNavigationOtherForYou(user.getUsername(), user.getCleanedEmail()));
        }
    }
}
