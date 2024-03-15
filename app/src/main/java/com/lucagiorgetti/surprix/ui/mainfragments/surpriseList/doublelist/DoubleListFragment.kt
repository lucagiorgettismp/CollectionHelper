package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.doublelist

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseListFragment
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseRecyclerAdapterListener
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao

class DoubleListFragment : BaseSurpriseListFragment() {
    private var doubleListViewModel: DoubleListViewModel? = null

    override fun deleteSurprise(mAdapter: SurpriseRecyclerAdapter?, position: Int) {
        val surprise = mAdapter!!.getItemAtPosition(position)!!
        mAdapter.removeFilterableItem(surprise)
        val doubleListDao = DoubleListDao(SurprixApplication.instance.currentUser?.username)
        val query = searchView!!.query
        if (query != null && query.isNotEmpty()) {
            mAdapter.filter.filter(query)
        } else {
            mAdapter.notifyItemRemoved(position)
        }
        doubleListDao.removeDouble(surprise.id)
        if (mAdapter.itemCount > 0) {
            emptyList!!.visibility = View.GONE
            setTitle(getString(R.string.doubles) + " (" + mAdapter.itemCount + ")")
        } else {
            emptyList!!.visibility = View.VISIBLE
            setTitle(getString(R.string.doubles))
        }
        if (query != null && query.isNotEmpty()) {
            Snackbar.make(requireView(), SurprixApplication.instance.getString(R.string.double_removed), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(requireView(), SurprixApplication.instance.getString(R.string.double_removed), Snackbar.LENGTH_LONG)
                    .setAction(SurprixApplication.instance.getString(R.string.discard_btn)) {
                        doubleListDao.addDouble(surprise.id)
                        mAdapter.addFilterableItem(surprise, position)
                        mAdapter.notifyItemInserted(position)
                        if (mAdapter.itemCount > 0) {
                            setTitle(getString(R.string.doubles) + " (" + mAdapter.itemCount + ")")
                            emptyList!!.visibility = View.GONE
                        } else {
                            setTitle(getString(R.string.doubles))
                            emptyList!!.visibility = View.VISIBLE
                        }
                    }.show()
        }
    }

    override fun setupData() {
        doubleListViewModel = ViewModelProvider(this)[DoubleListViewModel::class.java]
        mAdapter = SurpriseRecyclerAdapter(SurpriseListType.DOUBLES)
        mAdapter!!.setListener(object : BaseSurpriseRecyclerAdapterListener {
            override fun onSurpriseDelete(position: Int) {
                deleteSurprise(mAdapter, position)
            }

            override fun onImageClicked(imagePath: String, imageView: ImageView, placeHolderId: Int) {
                zoomImageFromThumb(imagePath, imageView, placeHolderId)
            }
        })
        doubleListViewModel!!.doubleSurprises.observe(viewLifecycleOwner) { doubleList: MutableList<Surprise> ->
            mAdapter!!.submitList(doubleList)
            mAdapter!!.setFilterableList(doubleList)
            mAdapter!!.notifyDataSetChanged()

            if (mAdapter!!.itemCount > 0) {
                setTitle(getString(R.string.doubles) + " (" + mAdapter!!.itemCount + ")")
                chipFilters = ChipFilters()
                chipFilters!!.initBySurprises(doubleList)
            } else {
                setTitle(getString(R.string.doubles))
            }
        }
        doubleListViewModel!!.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                emptyList!!.visibility = View.GONE
                showLoading()
            } else {
                hideLoading()

                if(mAdapter!!.itemCount <= 0){
                    emptyList!!.visibility = View.VISIBLE
                }
                swipeRefreshLayout!!.isRefreshing = false
            }
        }
    }

    override fun setupView() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.filter_search_menu, menu)
                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView?
                searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(s: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        mAdapter!!.filter.filter(s)
                        return false
                    }
                })
                searchView!!.queryHint = getString(R.string.search)
                searchView!!.setOnCloseListener {
                    if (mAdapter!!.itemCount > 0) {
                        setTitle(getString(R.string.doubles) + " (" + mAdapter!!.itemCount + ")")
                    } else {
                        setTitle(getString(R.string.doubles))
                    }
                    false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_filter -> {
                        openBottomSheet()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun loadData() {
        doubleListViewModel!!.loadDoubleSurprises()
    }

    override fun onStart() {
        loadData()
        super.onStart()
    }
}
