package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

abstract class BaseSetListFragment : BaseFragment() {
    var mAdapter: SetRecyclerAdapter? = null
    var recyclerView: RecyclerView? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_set_list, container, false)
        mAdapter = setAdapter()
        val progress = root.findViewById<ProgressBar>(R.id.set_loading)
        setProgressBar(progress)
        recyclerView = root.findViewById(R.id.set_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener { reloadData() })

        setupView()
        return root
    }

    //protected abstract void loadData();
    protected abstract fun reloadData()
    protected abstract fun setupView()
    protected abstract fun setAdapter(): SetRecyclerAdapter
}
