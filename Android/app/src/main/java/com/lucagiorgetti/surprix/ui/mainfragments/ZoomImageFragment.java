package com.lucagiorgetti.surprix.ui.mainfragments;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class ZoomImageFragment extends BaseFragment {
    private Animator currentAnimator = null;

    public void zoomImageFromThumb(String path, ImageView imageView, int placeHolderId){
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        if (getResources().getBoolean(R.bool.isTablet) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            final ImageView expandedImageView = getView().findViewById(R.id.surp_image_expanded);
            final ConstraintLayout imageContainer = getView().findViewById(R.id.surp_expanded_container);

            int height = imageContainer.getHeight();
            if (expandedImageView.getHeight() > height){
                expandedImageView.setMaxHeight(height);
                expandedImageView.setMaxWidth(height * 360 / 440);
            }

            SystemUtils.loadImage(path, expandedImageView, placeHolderId);

            final Rect startBounds = new Rect();
            final Rect finalBounds = new Rect();
            final Point globalOffset = new Point();

            imageView.getGlobalVisibleRect(startBounds);
            imageContainer.getGlobalVisibleRect(finalBounds, globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            imageView.setAlpha(0f);
            imageContainer.setVisibility(View.VISIBLE);
            expandedImageView.setVisibility(View.VISIBLE);

            expandedImageView.setOnClickListener(view -> {
                imageView.setAlpha(1f);
                imageContainer.setVisibility(View.GONE);
                expandedImageView.setVisibility(View.GONE);
            });
        }
    }
}
