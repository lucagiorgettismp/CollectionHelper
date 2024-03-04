package com.lucagiorgetti.surprix.ui.mainfragments

import android.animation.Animator
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

open class ZoomImageFragment : BaseFragment() {
    private val currentAnimator: Animator? = null
    fun zoomImageFromThumb(path: String?, imageView: ImageView, placeHolderId: Int) {
        currentAnimator?.cancel()
        if (resources.getBoolean(R.bool.isTablet) || resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val expandedImageView = requireView().findViewById<ImageView>(R.id.surp_image_expanded)
            val imageContainer = requireView().findViewById<ConstraintLayout>(R.id.surp_expanded_container)
            val height = imageContainer.height
            if (expandedImageView.height > height) {
                expandedImageView.maxHeight = height
                expandedImageView.maxWidth = height * 360 / 440
            }
            SystemUtils.loadImage(path, expandedImageView, placeHolderId)
            val startBounds = Rect()
            val finalBounds = Rect()
            val globalOffset = Point()
            imageView.getGlobalVisibleRect(startBounds)
            imageContainer.getGlobalVisibleRect(finalBounds, globalOffset)
            startBounds.offset(-globalOffset.x, -globalOffset.y)
            finalBounds.offset(-globalOffset.x, -globalOffset.y)
            imageView.alpha = 0f
            imageContainer.visibility = View.VISIBLE
            expandedImageView.visibility = View.VISIBLE
            expandedImageView.setOnClickListener { view: View? ->
                imageView.alpha = 1f
                imageContainer.visibility = View.GONE
                expandedImageView.visibility = View.GONE
            }
        }
    }
}
