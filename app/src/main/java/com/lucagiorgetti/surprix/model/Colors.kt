package com.lucagiorgetti.surprix.model

import com.lucagiorgetti.surprix.R

/**
 * Created by Luca on 06/11/2017.
 */
object Colors {
    private const val LIGHT_BLUE = "Light_blue"
    private const val BLUE = "Blue"
    const val RED = "Red"
    const val ORANGE = "Orange"
    private const val PURPLE = "Purple"
    private const val PINK = "Pink"
    private const val YELLOW = "Yellow"
    private const val GREEN = "Green"
    private const val LIGHT_GREEN = "Light_green"
    private const val BROWN = "Brown"
    private const val GREY = "Grey"
    fun getHexColor(color: String?): Int {
        var c = -2
        when (color) {
            LIGHT_BLUE -> c = R.color.LightBluSet
            BLUE -> c = R.color.BlueSet
            RED -> c = R.color.RedSet
            ORANGE -> c = R.color.OrangeSet
            PURPLE -> c = R.color.PurpleSet
            PINK -> c = R.color.PinkSet
            YELLOW -> c = R.color.YellowSet
            GREEN -> c = R.color.GreenSet
            LIGHT_GREEN -> c = R.color.LightGreenSet
            GREY -> c = R.color.GreySet
            BROWN -> c = R.color.BrownSet
        }
        return c
    }

    fun getDarkHexColor(color: String?): Int {
        var c = -2
        when (color) {
            LIGHT_BLUE -> c = R.color.DarkLightBluSet
            BLUE -> c = R.color.DarkBlueSet
            RED -> c = R.color.DarkRedSet
            ORANGE -> c = R.color.DarkOrangeSet
            PURPLE -> c = R.color.DarkPurpleSet
            PINK -> c = R.color.DarkPinkSet
            YELLOW -> c = R.color.DarkYellowSet
            GREEN -> c = R.color.DarkGreenSet
            LIGHT_GREEN -> c = R.color.DarkLightGreenSet
            GREY -> c = R.color.DarkGreySet
            BROWN -> c = R.color.DarkBrownSet
        }
        return c
    }
}
