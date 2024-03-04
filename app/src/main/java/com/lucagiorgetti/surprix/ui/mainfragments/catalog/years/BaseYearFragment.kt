package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

abstract class BaseYearFragment : BaseFragment() {
    var recyclerView: RecyclerView? = null
    var mAdapter: YearRecyclerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_years, container, false)
        val progress = root.findViewById<ProgressBar>(R.id.year_loading)
        setProgressBar(progress)
        recyclerView = root.findViewById(R.id.year_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.setLayoutManager(layoutManager)
        mAdapter = YearRecyclerAdapter()
        recyclerView!!.setAdapter(mAdapter)
        setupView()
        return root
    }

    abstract fun setupView()
}
