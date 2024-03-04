package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.SurprixLocales
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.ui.mainfragments.filter.ChipFilters
import com.lucagiorgetti.surprix.ui.mainfragments.filter.FilterType
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

/**
 * Adapter for showing a list of Sets.
 *
 *
 * Created by Luca on 24/10/2017.
 */
class SetRecyclerAdapter(private val navigationMode: CatalogNavigationMode?, private val listener: SetListFragment.MyClickListener) : ListAdapter<CatalogSet, SetRecyclerAdapter.SetViewHolder>(DIFF_CALLBACK), Filterable {
    private var filterableList: List<CatalogSet>? = null
    var searchViewFilteredValues: List<CatalogSet> = ArrayList()
    private var chipFilters: ChipFilters? = null
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SetViewHolder {
        val v: View
        v = if (navigationMode == CatalogNavigationMode.COLLECTION) {
            LayoutInflater.from(parent.context).inflate(R.layout.element_collection_set, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.element_set, parent, false)
        }
        return SetViewHolder(v, navigationMode)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = getItem(position).set
        holder.vName.text = set.name
        holder.vNation.setText(SurprixLocales.Companion.getDisplayName(set.nation?.lowercase(Locale.getDefault())))
        if (navigationMode == CatalogNavigationMode.COLLECTION) {
            val cs = getItem(position)
            if (cs!!.hasMissing()) {
                holder.setHasMissing!!.visibility = View.VISIBLE
                holder.setComplete!!.visibility = View.GONE
            } else {
                holder.setHasMissing!!.visibility = View.GONE
                holder.setComplete!!.visibility = View.VISIBLE
            }
        } else {
            holder.myCollectionSwitch!!.setOnClickListener { v: View? ->
                val isChecked = holder.myCollectionSwitch!!.isChecked
                listener.onSetInCollectionChanged(set, isChecked, position)
            }
            val inCollection = getItem(position)!!.isInCollection
            holder.myCollectionSwitch!!.isChecked = inCollection
            val onLongClick = OnLongClickListener { v: View? -> listener.onSetLongClicked(set, holder.myCollectionSwitch!!) }
            holder.vImage.setOnLongClickListener(onLongClick)
            holder.clickableZone.setOnLongClickListener(onLongClick)
        }
        val onClick = View.OnClickListener { v: View? -> listener.onSetClicked(set, position) }
        holder.vImage.setOnClickListener(onClick)
        holder.clickableZone.setOnClickListener(onClick)
        val path = set.img_path
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_bpz_placeholder)
    }

    fun getItemAtPosition(position: Int): CatalogSet {
        return getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setFilterableList(sets: List<CatalogSet>) {
        filterableList = sets
        searchViewFilteredValues = sets
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredValues: MutableList<CatalogSet?> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredValues.addAll(filterableList!!)
            } else {
                val pattern = charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (set in filterableList!!) {
                    if (set.set.code?.lowercase(Locale.getDefault())!!.contains(pattern)
                            || set.set.name?.lowercase(Locale.getDefault())!!.contains(pattern)) {
                        filteredValues.add(set)
                    }
                }
            }
            val results = FilterResults()
            results.values = applyFilter(filteredValues)
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            searchViewFilteredValues = filterResults.values as List<CatalogSet>
            submitList(searchViewFilteredValues)
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun setChipFilters(selection: ChipFilters?) {
        chipFilters = selection
        submitList(applyFilter(searchViewFilteredValues))
    }

    private fun applyFilter(values: List<CatalogSet?>?): List<CatalogSet?>? {
        return if (chipFilters == null) {
            values
        } else {
            val returnList: MutableList<CatalogSet?> = ArrayList()
            for (surprise in values!!) {
                if (chipFilters!!.getFiltersByType(FilterType.COMPLETION).get(if (surprise!!.hasMissing()) ChipFilters.Companion.COMPLETION_NON_COMPLETED else ChipFilters.Companion.COMPLETION_COMPLETED)!!.isSelected) {
                    returnList.add(surprise)
                }
            }
            returnList
        }
    }

    fun notifyItemChecked(position: Int, checked: Boolean) {
        if (filterableList != null && !filterableList!!.isEmpty()) {
            filterableList!![position]?.isInCollection = checked
            notifyItemChanged(position)
        }
    }

    class SetViewHolder(v: View, navigationMode: CatalogNavigationMode?) : RecyclerView.ViewHolder(v) {
        var vName: TextView
        var vNation: TextView
        var vImage: ImageView
        var clickableZone: View
        var myCollectionSwitch: SwitchMaterial? = null
        var setHasMissing: ImageView? = null
        var setComplete: ImageView? = null

        init {
            vName = v.findViewById(R.id.txv_set_elem_name)
            vImage = v.findViewById(R.id.imgSet)
            vNation = v.findViewById(R.id.txv_set_elem_nation)
            clickableZone = v.findViewById(R.id.clickable_zone)
            when (navigationMode) {
                CatalogNavigationMode.CATALOG -> myCollectionSwitch = v.findViewById(R.id.my_collection_switch)
                CatalogNavigationMode.COLLECTION -> {
                    setHasMissing = v.findViewById(R.id.set_miss)
                    setComplete = v.findViewById(R.id.set_check)
                }

                else -> {}
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<CatalogSet> = object : DiffUtil.ItemCallback<CatalogSet>() {
            override fun areItemsTheSame(oldItem: CatalogSet, newItem: CatalogSet): Boolean {
                return oldItem.set.id == newItem.set.id && newItem.isInCollection == oldItem.isInCollection
            }

            override fun areContentsTheSame(oldItem: CatalogSet, newItem: CatalogSet): Boolean {
                return oldItem.set.id == newItem.set.id && newItem.isInCollection == oldItem.isInCollection
            }
        }
    }
}
