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
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter.SurpriseViewHolder
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

class SurpriseRecyclerAdapter private constructor() : ListAdapter<Surprise, SurpriseViewHolder>(DIFF_CALLBACK), Filterable {
    private var listener: BaseSurpriseRecyclerAdapterListener? = null
    private var filterableList: MutableList<Surprise>? = null
    var searchViewFilteredValues: List<Surprise> = ArrayList()
    private var chipFilters: ChipFilters? = null
    private var type: SurpriseListType? = null

    constructor(type: SurpriseListType?) : this() {
        this.type = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurpriseViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_surprise_list, parent, false)
        return SurpriseViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SurpriseViewHolder, position: Int) {
        val surprise = getItem(position)
        holder.vSetName.text = surprise.set_name
        if (surprise!!.isSet_effective_code) {
            holder.vDescription.text = surprise.code + " - " + surprise.description
        } else {
            holder.vDescription.text = surprise.description
        }
        holder.vYear.text = surprise.set_year_name
        holder.vProducer.text = surprise.set_producer_name
        holder.vNation.text = SurprixLocales.getDisplayName(surprise.set_nation?.lowercase(Locale.getDefault()))
        val path = surprise.img_path!!
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_logo_shape_primary)
        when (type) {
            SurpriseListType.MISSINGS -> {
                holder.vBtnOwners.visibility = View.VISIBLE
                holder.vBtnOwners.setOnClickListener { (listener as MissingRecyclerAdapterListener?)!!.onShowMissingOwnerClick(surprise) }
                holder.delete.visibility = View.VISIBLE
                holder.delete.setOnClickListener { listener!!.onSurpriseDelete(position) }
                holder.vImage.setOnClickListener { listener!!.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary) }
            }

            SurpriseListType.DOUBLES -> {
                holder.vBtnOwners.visibility = View.GONE
                holder.delete.visibility = View.VISIBLE
                holder.delete.setOnClickListener { listener!!.onSurpriseDelete(position) }
                holder.vImage.setOnClickListener { listener!!.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary) }
            }

            SurpriseListType.SEARCH -> {
                holder.delete.visibility = View.GONE
                holder.vBtnOwners.visibility = View.GONE
            }

            else -> {}
        }
        val rarity = surprise.intRarity
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
            if (charSequence.isEmpty()) {
                filteredValues.addAll(filterableList!!)
            } else {
                val pattern = charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (surprise in filterableList!!) {
                    if (surprise.code?.lowercase(Locale.getDefault())!!.contains(pattern)
                            || surprise.description?.lowercase(Locale.getDefault())!!.contains(pattern)
                            || surprise.set_name?.lowercase(Locale.getDefault())!!.contains(pattern)) {
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

    inner class SurpriseViewHolder(v: View) : RecyclerView.ViewHolder(v) {
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
