package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.model.Year
import com.lucagiorgetti.surprix.model.Year.SortByDescYear
import java.util.Collections

/**
 * Adapter for showing a list of Years.
 *
 *
 * Created by Luca on 24/10/2017.
 */
class YearRecyclerAdapter : RecyclerView.Adapter<YearRecyclerAdapter.SetViewHolder>() {
    private var years: List<Year?>? = null
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SetViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.element_year, parent, false)
        return SetViewHolder(v)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val year = years!![position]
        holder.vName.text = year?.descr
    }

    override fun getItemCount(): Int {
        return if (years != null) {
            years!!.size
        } else 0
    }

    fun getItemAtPosition(position: Int): Year? {
        return years!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setYears(years: List<Year>) {
        Collections.sort(years, SortByDescYear())
        this.years = years
    }

    class SetViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vName: TextView

        init {
            vName = v.findViewById(R.id.txv_year_number)
        }
    }
}
