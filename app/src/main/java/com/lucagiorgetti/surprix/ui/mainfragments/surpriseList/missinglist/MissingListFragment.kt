package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.missinglist

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
import androidx.navigation.Navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.BaseSurpriseListFragment
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.MissingRecyclerAdapterListener
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class MissingListFragment : BaseSurpriseListFragment() {
    private var missingListViewModel: MissingListViewModel? = null

    override fun deleteSurprise(mAdapter: SurpriseRecyclerAdapter?, position: Int) {
        val missingListDao = MissingListDao(SurprixApplication.instance.currentUser?.username)
        val surprise = mAdapter!!.getItemAtPosition(position)!!
        val query = searchView!!.query
        missingListDao.removeMissing(surprise.id)
        mAdapter.removeFilterableItem(surprise)
        if (query != null && query.isNotEmpty()) {
            mAdapter.filter.filter(query)
        } else {
            mAdapter.notifyItemRemoved(position)
        }
        if (mAdapter.itemCount > 0) {
            emptyList!!.visibility = View.GONE
            setTitle(getString(R.string.missings) + " (" + mAdapter.itemCount + ")")
        } else {
            emptyList!!.visibility = View.VISIBLE
            setTitle(getString(R.string.missings))
        }
        if (query != null && query.isNotEmpty()) {
            Snackbar.make(requireView(), SurprixApplication.instance.getString(R.string.missing_removed), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(requireView(), SurprixApplication.instance.getString(R.string.missing_removed), Snackbar.LENGTH_LONG)
                    .setAction(SurprixApplication.instance.getString(R.string.discard_btn)) {
                        missingListDao.addMissing(surprise.id)
                        mAdapter.addFilterableItem(surprise, position)
                        mAdapter.notifyItemInserted(position)
                        if (mAdapter.itemCount > 0) {
                            emptyList!!.visibility = View.GONE
                            setTitle(getString(R.string.missings) + " (" + mAdapter.itemCount + ")")
                        } else {
                            emptyList!!.visibility = View.VISIBLE
                            setTitle(getString(R.string.missings))
                        }
                    }.show()
        }
    }

    override fun setupData() {
        missingListViewModel = ViewModelProvider(this)[MissingListViewModel::class.java]
        mAdapter = SurpriseRecyclerAdapter(SurpriseListType.MISSINGS)
        mAdapter!!.setListener(object : MissingRecyclerAdapterListener {
            override fun onShowMissingOwnerClick(surprise: Surprise) {
                findNavController(root!!).navigate(MissingListFragmentDirections.actionNavigationMissingListToNavigationMissingOwners(surprise.id!!))
            }

            override fun onSurpriseDelete(position: Int) {
                deleteSurprise(mAdapter, position)
            }

            override fun onImageClicked(imagePath: String, imageView: ImageView, placeHolderId: Int) {
                zoomImageFromThumb(imagePath, imageView, placeHolderId)
            }
        })
        missingListViewModel!!.missingSurprises.observe(viewLifecycleOwner) { missingList: MutableList<Surprise>? ->
            emptyList!!.visibility = if (missingList!!.isEmpty()) View.VISIBLE else View.GONE
            mAdapter!!.submitList(missingList)
            mAdapter!!.setFilterableList(missingList)
            if (mAdapter!!.itemCount > 0) {
                setTitle(getString(R.string.missings) + " (" + mAdapter!!.itemCount + ")")
                chipFilters = ChipFilters()
                chipFilters!!.initBySurprises(missingList)
            } else {
                setTitle(getString(R.string.missings))
                //fab.setVisibility(View.GONE);
            }
        }
        missingListViewModel!!.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                emptyList!!.visibility = View.GONE
                showLoading()
            } else {
                hideLoading()
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
                        setTitle(getString(R.string.missings) + " (" + mAdapter!!.itemCount + ")")
                    } else {
                        setTitle(getString(R.string.missings))
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
        missingListViewModel!!.loadMissingSurprises()
    }
}