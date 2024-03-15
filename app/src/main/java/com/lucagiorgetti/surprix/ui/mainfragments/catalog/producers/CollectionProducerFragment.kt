package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Producer
import com.lucagiorgetti.surprix.ui.activities.SettingsActivity
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener
import com.lucagiorgetti.surprix.utility.SystemUtils

class CollectionProducerFragment : BaseProducerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.my_collection_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.nav_settings -> {
                        SystemUtils.openNewActivity(SettingsActivity::class.java, null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun setupView() {
        val producerViewModel = ViewModelProvider(this)[ProducerViewModel::class.java]
        emptyListText!!.setText(R.string.collection_producer_empty)
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(context, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val producer = mAdapter!!.getItemAtPosition(position)!!
                val prodName = producer.name!!
                val action = CollectionProducerFragmentDirections.actionCollectionProducerFragmentToNavigationYear(producer.id!!, prodName, CatalogNavigationMode.COLLECTION)
                findNavController(view!!).navigate(action)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        })
        )
        producerViewModel.getProducers(CatalogNavigationMode.COLLECTION).observe(viewLifecycleOwner) { producers: List<Producer?>? ->
            emptyList!!.visibility = if (producers.isNullOrEmpty()) View.VISIBLE else View.GONE
            mAdapter!!.setYears(producers)
            mAdapter!!.notifyDataSetChanged()
        }
        producerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
                emptyList!!.visibility = View.GONE
            } else {
                hideLoading()
            }
        }
        fab!!.visibility = View.GONE
    }
}