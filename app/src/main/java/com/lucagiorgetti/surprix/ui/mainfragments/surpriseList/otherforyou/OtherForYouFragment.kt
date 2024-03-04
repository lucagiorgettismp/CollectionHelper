package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.otherforyou

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

class OtherForYouFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_other_for_you, container, false)
        val mViewModel = ViewModelProvider(this).get(OtherForYouViewModel::class.java)
        val ownerUsername = OtherForYouFragmentArgs.fromBundle(requireArguments()).ownerUsername
        val ownerEmail = OtherForYouFragmentArgs.fromBundle(requireArguments()).ownerEmail
        val recyclerView = root.findViewById<RecyclerView>(R.id.other_for_you_recycler)
        val progress = root.findViewById<ProgressBar>(R.id.other_for_you_loading)
        setProgressBar(progress)
        val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener { mViewModel.loadOtherForYou(ownerUsername) }
        recyclerView.layoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        val adapter = SurpriseRecyclerAdapter(SurpriseListType.SEARCH)
        recyclerView.adapter = adapter
        val fab = root.findViewById<FloatingActionButton>(R.id.other_for_you_fab_mail)
        mViewModel.getOtherForYou(ownerUsername).observe(viewLifecycleOwner) { surprises: List<Surprise> ->
            if (!surprises.isEmpty()) {
                fab.visibility = View.VISIBLE
                adapter.submitList(surprises)
                adapter.notifyDataSetChanged()
                fab.setOnClickListener { v: View? -> sendEmailToUser(ownerEmail, surprises) }
            } else {
                fab.visibility = View.GONE
            }
        }
        mViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
                swipeRefreshLayout.isRefreshing = false
            }
        }
        setTitle(String.format(Locale.getDefault(), getString(R.string.other_for_you_title), ownerUsername))
        return root
    }

    private fun sendEmailToUser(ownerEmail: String, surprises: List<Surprise>) {
        val currentUser = getInstance().currentUser
        val subject = getInstance().getString(R.string.mail_subject, currentUser?.username)
        val sb = StringBuilder()
        sb.append("<ul>")
        for (surprise in surprises) {
            sb.append(String.format(Locale.getDefault(), "<li>%s - %s</li>", surprise.code, surprise.description))
        }
        sb.append("</ul>")
        val html = sb.toString()
        val body: Spanned
        body = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
        sendMail(ownerEmail, subject, body)
    }

    private fun sendMail(recipient: String, subject: String, html_body: Spanned) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setType("message/rfc822")
        intent.setData(Uri.parse(String.format("mailto:%s?subject=%s&body=%s", recipient, subject, html_body)))
        startActivity(Intent.createChooser(intent, getString(R.string.email_app_chooser)))
    }
}
