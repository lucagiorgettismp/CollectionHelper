package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.filter.CollectionSetFilterBSDFragment
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterBottomSheetListener
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.CollectionDao
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class SetListFragment : BaseSetListFragment() {
    var navigationMode: CatalogNavigationMode? = null
    var yearId: String? = null
    var yearName: String? = null
    var producerId: String? = null
    var collectionDao: CollectionDao? = null
    var missingListDao: MissingListDao? = null
    var setListViewModel: SetListViewModel? = null
    private var chipFilters: ChipFilters? = null
    var sharedViewModel: SharedViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val username = getInstance().currentUser?.username
        collectionDao = CollectionDao(username)
        missingListDao = MissingListDao(username)
        if (arguments != null) {
            yearId = SetListFragmentArgs.fromBundle(requireArguments()).yearId
            producerId = SetListFragmentArgs.fromBundle(requireArguments()).producerId
            yearName = SetListFragmentArgs.fromBundle(requireArguments()).yearName
            navigationMode = SetListFragmentArgs.fromBundle(requireArguments()).navigationMode
            if (navigationMode == CatalogNavigationMode.CATALOG && !SystemUtils.isSetHintDisplayed) {
                showHintAlert()
            }
        }
        super.onCreate(savedInstanceState)
    }

    private fun showHintAlert() {
        val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.set_hint_dialog_title)
                .setMessage(R.string.set_hint_dialog_message)
                .setPositiveButton(R.string.btn_ok_thanks) { dialog1: DialogInterface, which: Int ->
                    SystemUtils.isSetHintDisplayed = true
                    dialog1.dismiss()
                }
                .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    public override fun reloadData() {
        setListViewModel!!.loadSets(yearId, producerId, navigationMode)
    }

    public override fun setupView() {
        setTitle(yearName)
        if (navigationMode == CatalogNavigationMode.CATALOG) {
            sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        }
        setListViewModel = ViewModelProvider(this).get(SetListViewModel::class.java)
        setListViewModel!!.getSets(yearId, producerId, navigationMode).observe(viewLifecycleOwner) { sets: List<CatalogSet> ->
            mAdapter!!.submitList(sets)
            mAdapter!!.setFilterableList(sets)
            if (navigationMode == CatalogNavigationMode.COLLECTION) {
                chipFilters = ChipFilters()
                chipFilters!!.initByCatalogSets(sets)
            }
        }
        setListViewModel!!.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
                swipeRefreshLayout!!.isRefreshing = false
            }
        }
        if (navigationMode == CatalogNavigationMode.CATALOG) {
            sharedViewModel!!.getChecked().observe(viewLifecycleOwner) { checked: Boolean -> mAdapter!!.notifyItemChecked(sharedViewModel!!.position, checked) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when (navigationMode) {
            CatalogNavigationMode.CATALOG -> inflater.inflate(R.menu.search_menu, menu)
            CatalogNavigationMode.COLLECTION -> inflater.inflate(R.menu.filter_search_menu, menu)
            else -> {}
        }
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            openBottomSheet()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openBottomSheet() {
        if (chipFilters != null) {
            val bottomSheetDialogFragment = CollectionSetFilterBSDFragment(chipFilters)
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
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "double_bottom_sheet")
        } else {
            Snackbar.make(requireView(), getInstance().getString(R.string.cannot_oper_filters), Snackbar.LENGTH_SHORT).show()
        }
    }

    public override fun setAdapter(): SetRecyclerAdapter {
        return SetRecyclerAdapter(navigationMode, object : MyClickListener {
            override fun onSetInCollectionChanged(set: Set, isChecked: Boolean, position: Int) {
                if (isChecked) {
                    collectionDao!!.addSetInCollection(set)
                    mAdapter!!.notifyItemChecked(position, true)
                } else {
                    alertRemoveCollection(set, position)
                }
            }

            override fun onSetClicked(set: Set, position: Int) {
                if (navigationMode == CatalogNavigationMode.CATALOG) {
                    sharedViewModel!!.position = position
                }
                val action = SetListFragmentDirections.setSelectedAction(set.id!!, set.name!!, navigationMode!!)
                findNavController(view!!).navigate(action)
                SystemUtils.closeKeyboard(activity)
            }

            override fun onSetLongClicked(set: Set, inCollection: SwitchMaterial): Boolean {
                onLongSetClicked(set, inCollection)
                return true
            }
        })
    }

    private fun alertRemoveCollection(set: Set?, position: Int) {
        val alertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.setTitle(getString(R.string.remove_from_collection_title))
        alertDialog.setMessage(getString(R.string.remove_from_collection_message))
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.proceed_btn)
        ) { dialog: DialogInterface?, which: Int ->
            missingListDao!!.removeItemsFromSet(set)
            collectionDao!!.removeSetInCollection(set)
            mAdapter!!.notifyItemChecked(position, false)
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
        ) { dialog: DialogInterface?, which: Int ->
            mAdapter!!.notifyItemChecked(position, true)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun onLongSetClicked(set: Set, inCollection: SwitchMaterial?) {
        val alertDialog = android.app.AlertDialog.Builder(activity).create()
        alertDialog.setTitle(getString(R.string.dialog_add_set_title))
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + set.name + "?")
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive)
        ) { dialog: DialogInterface?, which: Int ->
            MissingListDao(getInstance().currentUser?.username).addMissingsBySet(set.id)
            inCollection!!.isChecked = true
            alertDialog.dismiss()
            Snackbar.make(requireView(), getInstance().getString(R.string.added_to_missings), Snackbar.LENGTH_SHORT).show()
        }
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
        ) { dialog: DialogInterface?, which: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }

    interface MyClickListener {
        fun onSetInCollectionChanged(set: Set, isChecked: Boolean, position: Int)
        fun onSetClicked(set: Set, position: Int)
        fun onSetLongClicked(set: Set, inCollection: SwitchMaterial): Boolean
    }
}