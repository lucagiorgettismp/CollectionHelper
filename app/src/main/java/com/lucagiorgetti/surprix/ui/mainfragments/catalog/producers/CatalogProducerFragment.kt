package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.lucagiorgetti.surprix.model.Producer
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener

class CatalogProducerFragment : BaseProducerFragment() {
    override fun setupView() {
        val producerViewModel = ViewModelProvider(this)[ProducerViewModel::class.java]
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(context, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val producer = mAdapter!!.getItemAtPosition(position)!!
                val prodName = producer.name!!
                val action = CatalogProducerFragmentDirections.producerSelectedAction(producer.id!!, prodName, CatalogNavigationMode.CATALOG)
                findNavController(view!!).navigate(action)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        })
        )
        producerViewModel.getProducers(CatalogNavigationMode.CATALOG).observe(viewLifecycleOwner) { producers: List<Producer?>? ->
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