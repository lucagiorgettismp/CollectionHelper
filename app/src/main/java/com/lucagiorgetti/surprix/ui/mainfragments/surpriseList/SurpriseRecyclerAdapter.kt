package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.model.SurprixLocales
import com.lucagiorgetti.surprix.ui.StarRank
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterType
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter.SurpViewHolder
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

class SurpriseRecyclerAdapter private constructor() : ListAdapter<Surprise, SurpViewHolder>(DIFF_CALLBACK), Filterable {
    private var listener: BaseSurpriseRecyclerAdapterListener? = null
    private var filterableList: MutableList<Surprise>? = null
    var searchViewFilteredValues: List<Surprise> = ArrayList()
    private var chipFilters: ChipFilters? = null
    private var type: SurpriseListType? = null

    constructor(type: SurpriseListType?) : this() {
        this.type = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurpViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_surprise_list, parent, false)
        return SurpViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SurpViewHolder, position: Int) {
        val surp = getItem(position)
        holder.vSetName.text = surp.set_name
        if (surp!!.isSet_effective_code) {
            holder.vDescription.text = surp.code + " - " + surp.description
        } else {
            holder.vDescription.text = surp.description
        }
        holder.vYear.text = surp.set_year_name
        holder.vProducer.text = surp.set_producer_name
        holder.vNation.text = SurprixLocales.Companion.getDisplayName(surp.set_nation?.lowercase(Locale.getDefault()))
        val path = surp.img_path!!
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_logo_shape_primary)
        when (type) {
            SurpriseListType.MISSINGS -> {
                holder.vBtnOwners.visibility = View.VISIBLE
                holder.vBtnOwners.setOnClickListener { view: View? -> (listener as MissingRecyclerAdapterListener?)!!.onShowMissingOwnerClick(surp) }
                holder.delete.visibility = View.VISIBLE
                holder.delete.setOnClickListener { v: View? -> listener!!.onSurpriseDelete(position) }
                holder.vImage.setOnClickListener { v: View? -> listener!!.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary) }
            }

            SurpriseListType.DOUBLES -> {
                holder.vBtnOwners.visibility = View.GONE
                holder.delete.visibility = View.VISIBLE
                holder.delete.setOnClickListener { v: View? -> listener!!.onSurpriseDelete(position) }
                holder.vImage.setOnClickListener { v: View? -> listener!!.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary) }
            }

            SurpriseListType.SEARCH -> {
                holder.delete.visibility = View.GONE
                holder.vBtnOwners.visibility = View.GONE
            }

            else -> {}
        }
        val rarity = surp.intRarity
        holder.vStar.setValue(rarity)
    }

    fun getItemAtPosition(position: Int): Surprise? {
        return getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredValues: MutableList<Surprise> = ArrayList()
            if (charSequence == null || charSequence.length == 0) {
                filteredValues.addAll(filterableList!!)
            } else {
                val pattern = charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (surprise in filterableList!!) {
                    if (surprise?.code?.lowercase(Locale.getDefault())!!.contains(pattern)
                            || surprise?.description?.lowercase(Locale.getDefault())!!.contains(pattern)
                            || surprise?.set_name?.lowercase(Locale.getDefault())!!.contains(pattern)) {
                        filteredValues.add(surprise)
                    }
                }
            }
            val results = FilterResults()
            results.values = applyFilter(filteredValues)
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            searchViewFilteredValues = filterResults.values as List<Surprise>
            submitList(searchViewFilteredValues)
        }
    }

    fun setFilterableList(missingList: MutableList<Surprise>) {
        filterableList = missingList
        searchViewFilteredValues = missingList
    }

    fun removeFilterableItem(surprise: Surprise?) {
        filterableList!!.remove(surprise)
    }

    fun addFilterableItem(surprise: Surprise, position: Int) {
        filterableList!!.add(position, surprise)
    }

    fun setListener(listener: BaseSurpriseRecyclerAdapterListener?) {
        this.listener = listener
    }

    fun setChipFilters(selection: ChipFilters?) {
        chipFilters = selection
        submitList(applyFilter(searchViewFilteredValues))
    }

    private fun applyFilter(values: List<Surprise>): List<Surprise> {
        return if (chipFilters == null) {
            values
        } else {
            val returnList: MutableList<Surprise> = ArrayList()
            for (surprise in values) {
                if (chipFilters!!.getFiltersByType(FilterType.CATEGORY)[surprise.set_category]!!.isSelected
                        && chipFilters!!.getFiltersByType(FilterType.YEAR)[surprise.set_year_year.toString()]!!.isSelected
                        && chipFilters!!.getFiltersByType(FilterType.PRODUCER)[surprise.set_producer_name]!!.isSelected) {
                    returnList.add(surprise)
                }
            }
            returnList
        }
    }

    inner class SurpViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vSetName: TextView
        var vDescription: TextView
        var vYear: TextView
        var vProducer: TextView
        var vNation: TextView
        var delete: TextView
        var vImage: ImageView
        var vStar: StarRank
        var vBtnOwners: Button

        init {
            vSetName = v.findViewById(R.id.txv_surp_elem_set)
            vDescription = v.findViewById(R.id.txv_surp_elem_desc)
            vYear = v.findViewById(R.id.txv_surp_elem_year)
            vProducer = v.findViewById(R.id.txv_surp_elem_producer)
            vNation = v.findViewById(R.id.txv_surp_elem_nation)
            vImage = v.findViewById(R.id.img_surp_elem)
            vStar = v.findViewById(R.id.star_rank)
            vBtnOwners = v.findViewById(R.id.show_owners)
            delete = v.findViewById(R.id.delete)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Surprise> = object : DiffUtil.ItemCallback<Surprise>() {
            override fun areItemsTheSame(oldItem: Surprise, newItem: Surprise): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Surprise, newItem: Surprise): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
