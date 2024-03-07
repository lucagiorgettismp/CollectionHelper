package com.lucagiorgetti.surprix.ui.mainfragments.catalog.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.model.SurprixLocales
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

/**
 * Adapter for showing a list of Sets.
 *
 *
 * Created by Luca on 24/10/2017.
 */
class SearchSetRecyclerAdapter internal constructor() : ListAdapter<Set, SearchSetRecyclerAdapter.SetViewHolder>(DIFF_CALLBACK), Filterable {
    private var filterableList: List<Set>? = null
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SetViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_set_search, parent, false)
        return SetViewHolder(v)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = getItem(position)
        holder.vName.text = set.name
        holder.vNation.text = SurprixLocales.getDisplayName(set.nation?.lowercase(Locale.getDefault()))
        holder.vProducer.text = set.producer_name
        holder.vYear.text = set.year_desc
        val path = set.img_path
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_bpz_placeholder)
    }

    fun getItemAtPosition(position: Int): Set? {
        return getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setFilterableList(sets: List<Set>?) {
        filterableList = sets
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<Set> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(filterableList!!)
            } else {
                val pattern = charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (set in filterableList!!) {
                    if (set.code?.lowercase(Locale.getDefault())!!.contains(pattern)
                            || set.name?.lowercase(Locale.getDefault())!!.contains(pattern)) {
                        filteredList.add(set)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            submitList(filterResults.values as List<Set>)
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    class SetViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vName: TextView
        var vNation: TextView
        var vProducer: TextView
        var vYear: TextView
        var vImage: ImageView

        init {
            vName = v.findViewById(R.id.txv_set_elem_name)
            vImage = v.findViewById(R.id.imgSet)
            vNation = v.findViewById(R.id.txv_set_elem_nation)
            vProducer = v.findViewById(R.id.txv_set_elem_producer)
            vYear = v.findViewById(R.id.txv_set_elem_year)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Set> = object : DiffUtil.ItemCallback<Set>() {
            override fun areItemsTheSame(oldItem: Set, newItem: Set): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Set, newItem: Set): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
