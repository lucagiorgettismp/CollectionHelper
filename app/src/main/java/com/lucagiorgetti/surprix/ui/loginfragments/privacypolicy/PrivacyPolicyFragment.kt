package com.lucagiorgetti.surprix.ui.loginfragments.privacypolicy

import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.lucagiorgetti.surprix.R

class PrivacyPolicyFragment : Fragment() {
    private var email: String? = null
    private var fromFacebook = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = PrivacyPolicyFragmentArgs.fromBundle(requireArguments()).email
        fromFacebook = PrivacyPolicyFragmentArgs.fromBundle(requireArguments()).fromFacebook
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_privacy_policy, container, false)
        val policyText = root.findViewById<TextView>(R.id.policy_text)
        policyText.movementMethod = ScrollingMovementMethod()

        val accept = root.findViewById<Button>(R.id.btn_accept)
        val refuse = root.findViewById<TextView>(R.id.btn_refuse)

        accept.setOnClickListener { v -> findNavController(v!!).navigate(PrivacyPolicyFragmentDirections.actionNavigationLoginPrivacyToNavigationLoginSignup(email, fromFacebook)) }
        refuse.setOnClickListener { v -> findNavController(v!!).popBackStack() }

        policyText.text = Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY)

        return root
    }
}
