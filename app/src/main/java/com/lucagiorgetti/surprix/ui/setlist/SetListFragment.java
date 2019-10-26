package com.lucagiorgetti.surprix.ui.setlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.adapters.SetRecyclerAdapter;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.views.SetsActivity;

public class SetListFragment extends Fragment {

    private SetListViewModel setListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setListViewModel =
                ViewModelProviders.of(this).get(SetListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_set_list, container, false);

        SetRecyclerAdapter mAdapter;
        ProgressBar progress = root.findViewById(R.id.set_loading);
        RecyclerView recyclerView = root.findViewById(R.id.set_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SetRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Set set = mAdapter.getItemAtPosition(position);
                        SetListFragmentDirections.SetSelectedAction action = SetListFragmentDirections.setSelectedAction(set.getId());
                        Navigation.findNavController(view).navigate(action);
                        SystemUtility.closeKeyboard(getActivity());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        String yearId = null;
        if (getArguments() != null) {
            yearId = getArguments().getString("year_id");
        }

        setListViewModel.getSets(yearId).observe(this, sets -> {
            mAdapter.setSets(sets);
            mAdapter.notifyDataSetChanged();
        });

        setListViewModel.isLoading().observe(this, isLoading -> {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}