package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.content.DialogInterface
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets.SharedViewModel
import com.lucagiorgetti.surprix.utility.dao.CollectionDao
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class SetDetailFragment : BaseSetDetailFragment() {
    private var missingListDao = MissingListDao(SurprixApplication.instance.currentUser?.username)
    private var doubleListDao = DoubleListDao(SurprixApplication.instance.currentUser?.username)
    private var collectionDao = CollectionDao(SurprixApplication.instance.currentUser?.username)
    private var navigationMode: CatalogNavigationMode? = null
    private var setId: String? = null

    override fun setupView() {
        val setDetailViewModel = ViewModelProvider(this)[SetDetailViewModel::class.java]
        if (arguments != null) {
            setId = SetDetailFragmentArgs.fromBundle(requireArguments()).setId
            val setName = SetDetailFragmentArgs.fromBundle(requireArguments()).setName
            navigationMode = SetDetailFragmentArgs.fromBundle(requireArguments()).navigationMode
            setTitle(setName)
            when (navigationMode) {
                CatalogNavigationMode.CATALOG -> {
                    val finalSetId = setId!!
                    mAdapter =
                        CatalogSetDetailRecyclerAdapter(object : CatalogSetDetailClickListener {
                            override fun onSurpriseAddedToDoubles(s: Surprise) {
                                doubleListDao.addDouble(s.id)
                                Snackbar.make(
                                    view!!,
                                    SurprixApplication.instance.getString(R.string.add_to_doubles) + ": " + s.description,
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(SurprixApplication.instance.getString(R.string.discard_btn)) {
                                        doubleListDao.removeDouble(
                                            s.id
                                        )
                                    }.show()
                            }

                            override fun onSurpriseAddedToMissings(s: Surprise) {
                                collectionDao.isSetInCollection(
                                    finalSetId,
                                    object : CallbackInterface<Boolean> {
                                        override fun onStart() {}
                                        override fun onSuccess(inCollection: Boolean) {
                                            if (inCollection) {
                                                addSurpriseToMissingList(s)
                                            } else {
                                                showAlertAddSetInCollection(setName, s)
                                            }
                                        }

                                        override fun onFailure() {}
                                    })
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

                CatalogNavigationMode.COLLECTION -> {
                    mAdapter = CollectionSetDetailRecyclerAdapter(object : SetDetailClickListener {
                        override fun onImageClicked(
                            imagePath: String,
                            imageView: ImageView,
                            placeHolderId: Int
                        ) {
                            zoomImageFromThumb(imagePath, imageView, placeHolderId)
                        }
                    })
                }

                else -> {}
            }
        }
        setDetailViewModel.getSurprises(setId, navigationMode)
            .observe(viewLifecycleOwner) { surprises: List<CollectionSurprise> ->
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

    private fun showAlertAddSetInCollection(setName: String, surprise: Surprise) {
        val alertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.setTitle(getString(R.string.add_in_collection_title))
        alertDialog.setMessage(
            String.format(
                getString(R.string.add_in_collection_message),
                surprise.description,
                setName
            )
        )
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.add_btn)
        ) { _: DialogInterface?, _: Int ->
            alertDialog.dismiss()
            collectionDao.addSetInCollection(setId)
            val sharedViewModel =
                ViewModelProvider(requireActivity())[SharedViewModel::class.java]
            sharedViewModel.setChecked(true)
            addSurpriseToMissingList(surprise)
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
        ) { _: DialogInterface?, _: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }

    private fun addSurpriseToMissingList(surprise: Surprise) {
        missingListDao.addMissing(surprise.id)
        Snackbar.make(
            requireView(),
            SurprixApplication.instance.getString(R.string.added_to_missings) + ": " + surprise.description,
            Snackbar.LENGTH_LONG
        )
            .setAction(SurprixApplication.instance.getString(R.string.discard_btn)) {
                missingListDao.removeMissing(
                    surprise.id
                )
            }.show()
    }
}