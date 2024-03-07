package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.ui.mainfragments.ZoomImageFragment
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetListener
import com.lucagiorgetti.surprix.ui.mainfragments.filter.SurpriseFilterBSDFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

abstract class BaseSurpriseListFragment : ZoomImageFragment() {
    var mAdapter: SurpriseRecyclerAdapter? = null
    var searchView: SearchView? = null
    var root: View? = null
    var emptyList: View? = null
    var chipFilters: ChipFilters? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_surprise_list, container, false)
        }
        emptyList = root!!.findViewById(R.id.double_empty_list)
        val progress = root!!.findViewById<ProgressBar>(R.id.double_loading)
        setProgressBar(progress)
        val recyclerView = root!!.findViewById<RecyclerView>(R.id.double_recycler)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView.layoutManager = layoutManager
        swipeRefreshLayout = root!!.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout!!.setOnRefreshListener {
            emptyList!!.visibility = View.GONE
            loadData()
        }

        setupView()
        setupData()
        recyclerView.adapter = mAdapter
        initSwipe(recyclerView)

        return root
    }

    abstract fun deleteSurprise(mAdapter: SurpriseRecyclerAdapter?, position: Int)
    abstract fun setupData()
    abstract fun setupView()
    abstract fun loadData()
    fun openBottomSheet() {
        if (chipFilters != null) {
            val bottomSheetDialogFragment = SurpriseFilterBSDFragment(chipFilters)
            bottomSheetDialogFragment.setListener(object : FilterBottomSheetListener {
                override fun onFilterChanged(selection: ChipFilters?) {
                    mAdapter!!.setChipFilters(selection)
                }

                override fun onFilterCleared() {
                    chipFilters!!.clearSelection()
                    mAdapter!!.setChipFilters(chipFilters)
                    bottomSheetDialogFragment.dismissAllowingStateLoss()
                }
            })
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "surprise_list_bottom_sheet")
        } else {
            Snackbar.make(requireView(), SurprixApplication.instance.getString(R.string.cannot_oper_filters), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initSwipe(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                deleteSurprise(mAdapter, position)
            }
        }).attachToRecyclerView(recyclerView)
    }
}
