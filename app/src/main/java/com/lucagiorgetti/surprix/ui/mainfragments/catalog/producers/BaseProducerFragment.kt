package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

abstract class BaseProducerFragment : BaseFragment() {
    var mAdapter: ProducerRecyclerAdapter? = null
    var recyclerView: RecyclerView? = null
    var fab: FloatingActionButton? = null
    var emptyList: View? = null
    var emptyListText: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_producer_list, container, false)
        val progress = root.findViewById<ProgressBar>(R.id.catalog_loading)
        setProgressBar(progress)
        fab = root.findViewById(R.id.catalog_fab_search)
        emptyList = root.findViewById(R.id.empty_list)
        emptyListText = root.findViewById(R.id.empty_list_text)
        recyclerView = root.findViewById(R.id.catalog_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager
        mAdapter = ProducerRecyclerAdapter()
        recyclerView!!.adapter = mAdapter
        setupView()
        return root
    }

    abstract fun setupView()
}
