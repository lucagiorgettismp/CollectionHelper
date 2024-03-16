package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.ZoomImageFragment
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class SetDetailFragment : ZoomImageFragment()  {
    private var missingListDao = MissingListDao(SurprixApplication.instance.currentUser?.username)
    private var doubleListDao = DoubleListDao(SurprixApplication.instance.currentUser?.username)
    private var mAdapter: SetDetailRecyclerAdapter? = null
    private var hAdapter: SetDetailHeaderAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var thanksToTextView: TextView? = null
    private var setId: String? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_set_detail, container, false)
        val progress = root.findViewById<ProgressBar>(R.id.set_detail_loading)
        setProgressBar(progress)
        thanksToTextView = root.findViewById(R.id.txv_thanks_to)
        recyclerView = root.findViewById(R.id.set_detail_recycler)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager

        setupView()

        val concatAdapter = ConcatAdapter(hAdapter, mAdapter)
        recyclerView!!.adapter = concatAdapter
        return root
    }

    fun setupView() {
        val setDetailViewModel = ViewModelProvider(this)[SetDetailViewModel::class.java]
        if (arguments != null) {
            setId = SetDetailFragmentArgs.fromBundle(requireArguments()).setId
            val setName = SetDetailFragmentArgs.fromBundle(requireArguments()).setName
            val setThanks = SetDetailFragmentArgs.fromBundle(requireArguments()).setThanks
            setTitle(setName)

            hAdapter = SetDetailHeaderAdapter()
            hAdapter!!.setThanksTo(setThanks)

            mAdapter =
                SetDetailRecyclerAdapter(object : SetDetailClickListener {
                    override fun onSurpriseAddedToDoubles(surprise: Surprise) {
                        doubleListDao.addDouble(surprise.id)
                        Snackbar.make(
                            requireView(),
                            SurprixApplication.instance.getString(R.string.add_to_doubles) + ": " + surprise.description,
                            Snackbar.LENGTH_LONG
                        ).setAction(SurprixApplication.instance.getString(R.string.discard_btn))
                        {
                            doubleListDao.removeDouble(
                                surprise.id
                            )
                        }.show()
                    }

                    override fun onSurpriseAddedToMissings(surprise: Surprise) {
                        missingListDao.addMissing(surprise.id)
                        Snackbar.make(
                            requireView(),
                            SurprixApplication.instance.getString(R.string.added_to_missings) + ": " + surprise.description,
                            Snackbar.LENGTH_LONG
                        ).setAction(SurprixApplication.instance.getString(R.string.discard_btn))
                        {
                            missingListDao.removeMissing(
                                surprise.id
                            )
                        }.show()
                    }

                    override fun onImageClicked(
                        imagePath: String,
                        imageView: ImageView,
                        placeHolderId: Int
                    ) {
                        zoomImageFromThumb(imagePath, imageView, placeHolderId)
                    }
                })
        }
        setDetailViewModel.getSurprises(setId)
            .observe(viewLifecycleOwner) { surprises: List<Surprise> ->
                mAdapter!!.setSurprises(surprises)
                mAdapter!!.notifyDataSetChanged()
            }

        setDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }
}