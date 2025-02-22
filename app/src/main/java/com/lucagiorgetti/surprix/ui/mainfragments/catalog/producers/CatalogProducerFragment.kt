package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Producer
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener
import com.lucagiorgetti.surprix.utility.SystemUtils

class CatalogProducerFragment : BaseFragment() {
    private var mAdapter: ProducerRecyclerAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var fab: FloatingActionButton? = null
    private var emptyList: View? = null
    private var emptyListText: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_producer_list, container, false)
        val progress = root.findViewById<ProgressBar>(R.id.catalog_loading)
        setProgressBar(progress)
        fab = root.findViewById(R.id.catalog_fab_search)
        emptyList = root.findViewById(R.id.empty_list)
        emptyListText = root.findViewById(R.id.empty_list_text)
        recyclerView = root.findViewById(R.id.catalog_recycler)
        //recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager
        mAdapter = ProducerRecyclerAdapter()
        recyclerView!!.adapter = mAdapter

        setupView()

        return root
    }

    private fun setupView() {
        val producerViewModel = ViewModelProvider(this)[ProducerViewModel::class.java]
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(context, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val producer = mAdapter!!.getItemAtPosition(position)!!
                val prodName = producer.name!!
                val action = CatalogProducerFragmentDirections.producerSelectedAction(producer.id!!, prodName)
                findNavController(view!!).navigate(action)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        })
        )
        producerViewModel.getProducers().observe(viewLifecycleOwner) { producers: List<Producer?>? ->
            emptyList!!.visibility = if (producers.isNullOrEmpty()) View.VISIBLE else View.GONE
            mAdapter!!.setYears(producers)
            mAdapter!!.notifyDataSetChanged()
        }
        producerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                emptyList!!.visibility = View.GONE
                showLoading()
            } else {
                hideLoading()
            }
        }
        fab!!.setOnClickListener { v-> findNavController(v!!).navigate(CatalogProducerFragmentDirections.goToSearch()) }
    }
}