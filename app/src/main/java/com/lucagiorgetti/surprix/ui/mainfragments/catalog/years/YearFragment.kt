package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.model.Year
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class YearFragment : BaseYearFragment() {
    var navigationMode: CatalogNavigationMode? = null
    override fun setupView() {
        val yearViewModel = ViewModelProvider(this).get(YearViewModel::class.java)
        var producerId: String? = null
        if (arguments != null) {
            producerId = YearFragmentArgs.fromBundle(requireArguments()).producerId
            val producerName = YearFragmentArgs.fromBundle(requireArguments()).producerName
            navigationMode = YearFragmentArgs.fromBundle(requireArguments()).navigationMode
            setTitle(producerName)
        }
        val finalProducerId = producerId
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(context, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val year = mAdapter!!.getItemAtPosition(position)!!
                val action = YearFragmentDirections.yearSelectedAction(year.id!!, year.descr!!, navigationMode!!, finalProducerId!!)
                findNavController(view!!).navigate(action)
                SystemUtils.closeKeyboard(activity)
            }

            override fun onLongItemClick(view: View?, position: Int) {
                val year = mAdapter!!.getItemAtPosition(position)!!
                onLongYearClicked(year.id, year.year)
                SystemUtils.closeKeyboard(activity)
            }
        })
        )
        yearViewModel.getYears(producerId, navigationMode).observe(viewLifecycleOwner) { years: List<Year> ->
            mAdapter!!.setYears(years)
            mAdapter!!.notifyDataSetChanged()
        }
        yearViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun onLongYearClicked(yearId: String?, year: Int) {
        val alertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.setTitle(getString(R.string.dialog_add_year_title))
        alertDialog.setMessage(getString(R.string.dialog_add_year_text) + " " + year + "?")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive)
        ) { dialog: DialogInterface?, which: Int ->
            MissingListDao(getInstance().currentUser?.username).addMissingsByYear(yearId)
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
        ) { dialog: DialogInterface?, which: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }
}