package com.lucagiorgetti.surprix.ui.mainfragments.missingowners

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

class MissingOwnersFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mViewModel = ViewModelProvider(this)[MissingOwnersViewModel::class.java]
        val surpriseId = MissingOwnersFragmentArgs.fromBundle(requireArguments()).missingSurpriseId
        val root = inflater.inflate(R.layout.fragment_missing_owners, container, false)
        val emptyList = root.findViewById<View>(R.id.missing_owner_empty_list)
        val recyclerView = root.findViewById<RecyclerView>(R.id.missing_owner_recycler)
        val progress = root.findViewById<ProgressBar>(R.id.missing_owner_loading)
        setProgressBar(progress)
        val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            emptyList.visibility = View.GONE
            mViewModel.loadOwners(surpriseId)
        }
        recyclerView.layoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        val adapter = MissingOwnersAdapter()
        adapter.setListener(MyClickListener(root))
        recyclerView.adapter = adapter
        mViewModel.getMissingOwners(surpriseId).observe(viewLifecycleOwner) { owners: List<User?>? ->
            emptyList.visibility = if (owners.isNullOrEmpty()) View.VISIBLE else View.GONE
            if (owners!!.isNotEmpty()) {
                adapter.setOwners(owners)
                adapter.notifyDataSetChanged()
            }
        }
        mViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
                swipeRefreshLayout.isRefreshing = false
            }
        }
        setTitle(getString(R.string.find_surprise))
        return root
    }

    class MyClickListener(var root: View) {
        fun onOwnerClicked(user: User?) {
            findNavController(root).navigate(MissingOwnersFragmentDirections.actionNavigationMissingOwnersToNavigationOtherForYou(user?.username!!, user.clearedEmail()))
        }
    }
}
