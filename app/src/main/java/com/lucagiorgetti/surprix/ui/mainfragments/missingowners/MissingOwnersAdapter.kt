package com.lucagiorgetti.surprix.ui.mainfragments.missingowners

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.User

/**
 * Adapter for showing a list of User which owns a selected surprise.
 *
 *
 * Created by Luca on 28/10/2017.
 */
class MissingOwnersAdapter : RecyclerView.Adapter<MissingOwnersAdapter.ViewHolder>() {
    private var items: List<User?>? = ArrayList()
    private var listener: MissingOwnersFragment.MyClickListener? = null
    fun setOwners(items: List<User?>?) {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_missing_owners, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items!![position]!!
        holder.vUsername.text = user.username
        holder.vCountry.text = user.country
        holder.vCard.setOnClickListener { listener!!.onOwnerClicked(user) }
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else items!!.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vUsername: TextView
        var vCountry: TextView
        var vCard: View

        init {
            vUsername = v.findViewById(R.id.txv_collector_username)
            vCountry = v.findViewById(R.id.txv_collector_country)
            vCard = v.findViewById(R.id.missing_owner_card)
        }
    }

    fun setListener(myClickListener: MissingOwnersFragment.MyClickListener?) {
        listener = myClickListener
    }
}
