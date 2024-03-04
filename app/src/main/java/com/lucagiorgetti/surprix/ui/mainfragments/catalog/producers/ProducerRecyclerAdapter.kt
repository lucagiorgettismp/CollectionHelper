package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Producer

/**
 * Adapter for showing a list of Producers.
 *
 *
 * Created by Luca on 24/10/2017.
 */
class ProducerRecyclerAdapter : RecyclerView.Adapter<ProducerRecyclerAdapter.SetViewHolder>() {
    private var producers: List<Producer?>? = null
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SetViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_producer, parent, false)
        return SetViewHolder(v)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val producer = producers!![position]!!
        holder.vName.text = producer.name
        holder.vProduct.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return if (producers == null) {
            0
        } else producers!!.size
    }

    fun getItemAtPosition(position: Int): Producer? {
        return producers!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setYears(producers: List<Producer?>?) {
        this.producers = producers
    }

    class SetViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vName: TextView
        var vProduct: TextView

        init {
            vName = v.findViewById(R.id.txv_title_prd_select)
            vProduct = v.findViewById(R.id.txv_subtitle_prd_select)
        }
    }
}
