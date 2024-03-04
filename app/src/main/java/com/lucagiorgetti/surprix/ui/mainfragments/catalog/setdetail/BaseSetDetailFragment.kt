package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.ui.mainfragments.ZoomImageFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

abstract class BaseSetDetailFragment : ZoomImageFragment() {
    var mAdapter: BaseSetDetailAdapter<*>? = null
    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_set_detail, container, false)
        val progress = root.findViewById<ProgressBar>(R.id.set_detail_loading)
        setProgressBar(progress)
        recyclerView = root.findViewById(R.id.set_detail_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.setLayoutManager(layoutManager)
        setupView()
        recyclerView!!.setAdapter(mAdapter)
        return root
    }

    abstract fun setupView()
}
