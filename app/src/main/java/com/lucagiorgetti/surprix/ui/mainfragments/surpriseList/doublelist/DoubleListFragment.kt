package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.doublelist

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseListFragment
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseRecyclerAdapterListener
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao

class DoubleListFragment : BaseSurpriseListFragment() {
    private var doubleListViewModel: DoubleListViewModel? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_search_menu, menu)
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
        searchView!!.setOnCloseListener {
            if (mAdapter!!.itemCount > 0) {
                setTitle(getString(R.string.doubles) + " (" + mAdapter!!.itemCount + ")")
            } else {
                setTitle(getString(R.string.doubles))
            }
            false
        }
        searchView!!.queryHint = getString(R.string.search)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun deleteSurprise(mAdapter: SurpriseRecyclerAdapter?, position: Int) {
        val surprise = mAdapter!!.getItemAtPosition(position)!!
        mAdapter.removeFilterableItem(surprise)
        val doubleListDao = DoubleListDao(getInstance().currentUser?.username)
        val query = searchView!!.query
        if (query != null && query.length != 0) {
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
            Snackbar.make(requireView(), getInstance().getString(R.string.double_removed), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(requireView(), getInstance().getString(R.string.double_removed), Snackbar.LENGTH_LONG)
                    .setAction(getInstance().getString(R.string.discard_btn)) { view: View? ->
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
        mAdapter = SurpriseRecyclerAdapter(SurpriseListType.DOUBLES)
        mAdapter!!.setListener(object : BaseSurpriseRecyclerAdapterListener {
            override fun onSurpriseDelete(position: Int) {
                deleteSurprise(mAdapter, position)
            }

            override fun onImageClicked(imagePath: String, imageView: ImageView, placeHolderId: Int) {
                zoomImageFromThumb(imagePath, imageView, placeHolderId)
            }
        })
        doubleListViewModel = ViewModelProvider(this).get(DoubleListViewModel::class.java)
        doubleListViewModel!!.doubleSurprises.observe(viewLifecycleOwner) { doubleList: MutableList<Surprise> ->
            emptyList!!.visibility = if (doubleList.isEmpty()) View.VISIBLE else View.GONE
            mAdapter!!.submitList(doubleList)
            mAdapter!!.setFilterableList(doubleList)
            if (mAdapter!!.itemCount > 0) {
                setTitle(getString(R.string.doubles) + " (" + mAdapter!!.itemCount + ")")
                if (doubleList != null) {
                    chipFilters = ChipFilters()
                    chipFilters!!.initBySurprises(doubleList)
                }
            } else {
                setTitle(getString(R.string.doubles))
            }
        }
        doubleListViewModel!!.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
                emptyList!!.visibility = View.GONE
            } else {
                hideLoading()
                swipeRefreshLayout!!.isRefreshing = false
            }
        }
        setTitle(getString(R.string.doubles))
    }

    override fun loadData() {
        doubleListViewModel!!.loadDoubleSurprises()
    }
}
