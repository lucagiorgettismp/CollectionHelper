package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.model.Year
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class YearFragment : BaseFragment() {
    var recyclerView: RecyclerView? = null
    var mAdapter: YearRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_years, container, false)
        val progress = root.findViewById<ProgressBar>(R.id.year_loading)
        setProgressBar(progress)
        recyclerView = root.findViewById(R.id.year_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager
        mAdapter = YearRecyclerAdapter()
        recyclerView!!.adapter = mAdapter

        setupView()

        return root
    }

    private fun setupView() {
        val yearViewModel = ViewModelProvider(this)[YearViewModel::class.java]
        var producerId: String? = null
        if (arguments != null) {
            producerId = YearFragmentArgs.fromBundle(requireArguments()).producerId
            val producerName = YearFragmentArgs.fromBundle(requireArguments()).producerName
            setTitle(producerName)
        }
        val finalProducerId = producerId
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(context, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val year = mAdapter!!.getItemAtPosition(position)!!
                val action = YearFragmentDirections.yearSelectedAction(year.id!!, year.descr!!, finalProducerId!!)
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
        yearViewModel.getYears(producerId).observe(viewLifecycleOwner) { years: List<Year> ->
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
        ) { _: DialogInterface?, _: Int ->
            MissingListDao(SurprixApplication.instance.currentUser?.username).addMissingsByYear(yearId)
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
        ) { _: DialogInterface?, _: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }
}