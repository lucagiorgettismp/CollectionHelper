package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class SetListFragment : BaseFragment() {
    private var yearId: String? = null
    private var yearName: String? = null
    private var producerId: String? = null
    private var missingListDao: MissingListDao? = null
    private var setListViewModel: SetListViewModel? = null
    private var mAdapter: SetRecyclerAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_set_list, container, false)
        mAdapter = setAdapter()
        val progress = root.findViewById<ProgressBar>(R.id.set_loading)
        setProgressBar(progress)
        recyclerView = root.findViewById(R.id.set_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout!!.setOnRefreshListener { reloadData() }

        setupView()
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val username = SurprixApplication.instance.currentUser?.username

        missingListDao = MissingListDao(username)
        if (arguments != null) {
            yearId = SetListFragmentArgs.fromBundle(requireArguments()).yearId
            producerId = SetListFragmentArgs.fromBundle(requireArguments()).producerId
            yearName = SetListFragmentArgs.fromBundle(requireArguments()).yearName
        }

        if (!SystemUtils.getHintDisplayed()) {
            showHintAlert()
        }

        super.onCreate(savedInstanceState)
    }

    private fun showHintAlert() {
        val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.set_hint_dialog_title)
                .setMessage(R.string.set_hint_dialog_message)
                .setPositiveButton(R.string.btn_ok_thanks) { dialog1: DialogInterface, _: Int ->
                    SystemUtils.setHintDisplayed(true)
                    dialog1.dismiss()
                }
                .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun reloadData() {
        setListViewModel!!.loadSets(yearId)
    }

    private fun setupView() {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.search_menu, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView?
                searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(s: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        mAdapter!!.filter.filter(s)
                        return false
                    }
                })
                searchView.queryHint = getString(R.string.search)
                searchView.setOnCloseListener {
                    if (mAdapter!!.itemCount > 0) {
                        setTitle(getString(R.string.missings) + " (" + mAdapter!!.itemCount + ")")
                    } else {
                        setTitle(getString(R.string.missings))
                    }
                    false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setTitle(yearName!!)

        setListViewModel = ViewModelProvider(this)[SetListViewModel::class.java]
        setListViewModel!!.getSets(yearId).observe(viewLifecycleOwner) { sets: List<Set> ->
            mAdapter!!.submitList(sets)
            mAdapter!!.setFilterableList(sets)
            mAdapter!!.notifyDataSetChanged()
        }

        setListViewModel!!.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
                swipeRefreshLayout!!.isRefreshing = false
            }
        }
    }

    private fun setAdapter(): SetRecyclerAdapter {
        return SetRecyclerAdapter(object : MyClickListener {

            override fun onSetClicked(set: Set) {
                val thanks = set.thanks_to?.split(",")?.toTypedArray()
                val action = SetListFragmentDirections.setSelectedAction(set.id!!, set.name!!, thanks)
                findNavController(view!!).navigate(action)
                SystemUtils.closeKeyboard(activity)
            }

            override fun onSetLongClicked(set: Set): Boolean {
                onLongSetClicked(set)
                return true
            }
        })
    }

    private fun onLongSetClicked(set: Set) {
        val alertDialog = android.app.AlertDialog.Builder(activity).create()
        alertDialog.setTitle(getString(R.string.dialog_add_set_title))
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + set.name + "?")
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive)
        ) { _: DialogInterface?, _: Int ->
            MissingListDao(SurprixApplication.instance.currentUser?.username).addMissingsBySet(set.id)
            alertDialog.dismiss()
            Snackbar.make(requireView(), SurprixApplication.instance.getString(R.string.added_to_missings), Snackbar.LENGTH_SHORT).show()
        }
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
        ) { _: DialogInterface?, _: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }

    interface MyClickListener {
        fun onSetClicked(set: Set)

        fun onSetLongClicked(set: Set): Boolean
    }
}