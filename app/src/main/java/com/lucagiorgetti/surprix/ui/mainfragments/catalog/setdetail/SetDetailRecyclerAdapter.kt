package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.StarRank
import com.lucagiorgetti.surprix.utility.SystemUtils
import java.util.Locale

/**
 * Adapter for showing a list of Surprises from a selected set.
 *
 *
 * Created by Luca on 28/10/2017.
 */
class SetDetailRecyclerAdapter(private val listener: SetDetailClickListener) : RecyclerView.Adapter<SetDetailRecyclerAdapter.SetDetailViewHolder>() {
    private var items: List<Surprise>? = null
    fun getItemAtPosition(position: Int): Surprise {
        return items!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SetDetailViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_set_detail, parent, false)
        return SetDetailViewHolder(v)
    }

    override fun onBindViewHolder(holder: SetDetailViewHolder, position: Int) {
        val surprise = items!![position]
        if (surprise.isSet_effective_code) {
            holder.vDescription.text = String.format(Locale.getDefault(), "%s - %s", surprise.code, surprise.description)
        } else {
            holder.vDescription.text = surprise.description
        }
        val path = surprise.img_path!!
        SystemUtils.loadImage(path, holder.vImage, R.drawable.ic_logo_shape_primary)
        val rarity = surprise.intRarity
        holder.vStarRank.setValue(rarity)
        holder.vAddMissing.setOnClickListener { listener.onSurpriseAddedToMissings(surprise) }
        holder.vAddDouble.setOnClickListener { listener.onSurpriseAddedToDoubles(surprise) }
        holder.vImage.setOnClickListener { listener.onImageClicked(path, holder.vImage, R.drawable.ic_logo_shape_primary) }
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else items!!.size
    }

    fun setSurprises(surprises: List<Surprise>) {
        items = surprises
    }

    class SetDetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vDescription: TextView
        var vImage: ImageView
        var vAddMissing: MaterialButton
        var vAddDouble: MaterialButton
        var vStarRank: StarRank

        init {
            vDescription = v.findViewById(R.id.txv_item_desc)
            vImage = v.findViewById(R.id.img_item)
            vAddMissing = v.findViewById(R.id.add_missing_btn)
            vAddDouble = v.findViewById(R.id.add_double_btn)
            vStarRank = v.findViewById(R.id.star_rank)
        }
    }
}
