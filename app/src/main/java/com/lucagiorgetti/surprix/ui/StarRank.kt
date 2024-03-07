package com.lucagiorgetti.surprix.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.lucagiorgetti.surprix.R

class StarRank(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var star1On: ImageView
    private var star1Off: ImageView
    private var star2On: ImageView
    private var star2Off: ImageView
    private var star3On: ImageView
    private var star3Off: ImageView

    init {
        inflate(context, R.layout.star_rank, this)
        star1On = findViewById(R.id.star_1_on)
        star1Off = findViewById(R.id.star_1_off)
        star2On = findViewById(R.id.star_2_on)
        star2Off = findViewById(R.id.star_2_off)
        star3On = findViewById(R.id.star_3_on)
        star3Off = findViewById(R.id.star_3_off)
    }

    fun setValue(value: Int?) {
        if (value != null) {
            when (value) {
                1 -> {
                    star1On.visibility = VISIBLE
                    star1Off.visibility = GONE
                }

                2 -> {
                    star1On.visibility = VISIBLE
                    star2On.visibility = VISIBLE
                    star1Off.visibility = GONE
                    star2Off.visibility = GONE
                }

                3 -> {
                    star1On.visibility = VISIBLE
                    star2On.visibility = VISIBLE
                    star3On.visibility = VISIBLE
                    star1Off.visibility = GONE
                    star2Off.visibility = GONE
                    star3Off.visibility = GONE
                }
            }
        } else {
            star1Off.visibility = VISIBLE
            star2Off.visibility = VISIBLE
            star3Off.visibility = VISIBLE
            star1On.visibility = GONE
            star2On.visibility = GONE
            star3On.visibility = GONE
        }
    }
}
