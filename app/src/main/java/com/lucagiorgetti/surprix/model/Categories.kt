package com.lucagiorgetti.surprix.model

import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication

/**
 * Created by Luca on 06/11/2017.
 */
object Categories {
    const val COMPO = "Compo"
    const val HANDPAINTED = "Hand_painted"
    
    fun getDescriptionByString(value: String?): String {
        when (value) {
            COMPO -> return SurprixApplication.instance.getString(R.string.compo)
            HANDPAINTED -> return SurprixApplication.instance.getString(R.string.handpainted)
        }
        return SurprixApplication.instance.getString(R.string.other)
    }
}
