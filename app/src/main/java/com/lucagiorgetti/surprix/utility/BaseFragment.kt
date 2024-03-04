package com.lucagiorgetti.surprix.utility

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.lucagiorgetti.surprix.ui.activities.MainActivity
import java.util.Objects

open class BaseFragment : Fragment() {
    private var progressBar: ProgressBar? = null
    protected fun setTitle(title: String?) {
        Objects.requireNonNull((requireActivity() as MainActivity).supportActionBar)?.title = title
    }

    protected fun setProgressBar(progressBar: ProgressBar?) {
        this.progressBar = progressBar
    }

    protected fun showLoading() {
        if (progressBar != null) {
            progressBar!!.visibility = View.VISIBLE
        }
    }

    protected fun hideLoading() {
        if (progressBar != null) {
            progressBar!!.visibility = View.INVISIBLE
        }
    }
}
