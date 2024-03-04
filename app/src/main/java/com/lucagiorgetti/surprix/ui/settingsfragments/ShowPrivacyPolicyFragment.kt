package com.lucagiorgetti.surprix.ui.settingsfragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lucagiorgetti.surprix.R

class ShowPrivacyPolicyFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_show_privacy_policy, container, false)
        val policyText = root.findViewById<TextView>(R.id.policy_text)
        policyText.movementMethod = ScrollingMovementMethod()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            policyText.text = Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY)
        } else {
            policyText.text = Html.fromHtml(getString(R.string.privacy_policy_text))
        }
        return root
    }
}
