package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication

class SetDetailHeaderAdapter:  RecyclerView.Adapter<SetDetailHeaderAdapter.SetDetailHeaderViewHolder>() {
    private var thanksTo: Array<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetDetailHeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_set_detail, parent, false)

        return SetDetailHeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetDetailHeaderViewHolder, position: Int) {
        holder.bind(thanksTo!!)
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun setThanksTo(thanks: Array<String>){
        thanksTo = thanks
    }

    class SetDetailHeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val vThanksTo: TextView

        fun bind(thanks: Array<String>) {
            if(!thanks.isEmpty()){
                vThanksTo.text = String.format(
                    SurprixApplication.instance.getString(R.string.thanks_to),
                    thanks.joinToString(", ")
                )

                vThanksTo.visibility = View.VISIBLE
            } else {
                vThanksTo.visibility = View.GONE
            }
        }

        init {
            vThanksTo = v.findViewById(R.id.txv_thanks_to)
        }
    }
}

