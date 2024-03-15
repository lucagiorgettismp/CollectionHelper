package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.ui.StarRank
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

/**
 * Adapter for showing a list of Surprises from a selected set.
 *
 *
 * Created by Luca on 28/10/2017.
 */
class CollectionSetDetailRecyclerAdapter(private val listener: SetDetailClickListener) : BaseSetDetailAdapter<CollectionSetDetailRecyclerAdapter.SetDetailViewHolder>() {
    private var items: List<CollectionSurprise>? = null
    fun getItemAtPosition(position: Int): CollectionSurprise {
        return items!![position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SetDetailViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_collection_set_detail, parent, false)
        return SetDetailViewHolder(v)
    }

    override fun onBindViewHolder(holder: SetDetailViewHolder, position: Int) {
        val s = items!![position].surprise
        if (s.isSet_effective_code) {
            holder.vDescription.text = String.format(Locale.getDefault(), "%s - %s", s.code, s.description)
        } else {
            holder.vDescription.text = s.description
        }
        val path = s.img_path!!
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_logo_shape_primary)
        holder.vImage.setOnClickListener { listener.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary) }
        val rarity = s.intRarity
        holder.vStarRank.setValue(rarity)
        if (items!![position].isMissing) {
            holder.miss.visibility = View.VISIBLE
            holder.check.visibility = View.GONE
        } else {
            holder.miss.visibility = View.GONE
            holder.check.visibility = View.VISIBLE
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else items!!.size
    }

    override fun setSurprises(surprises: List<CollectionSurprise>) {
        items = surprises
    }

    class SetDetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vDescription: TextView
        var vImage: ImageView
        var vStarRank: StarRank
        var miss: ImageView
        var check: ImageView

        init {
            vDescription = v.findViewById(R.id.txv_item_desc)
            vImage = v.findViewById(R.id.img_item)
            vStarRank = v.findViewById(R.id.star_rank)
            miss = v.findViewById(R.id.set_detail_miss)
            check = v.findViewById(R.id.set_detail_check)
        }
    }
}
