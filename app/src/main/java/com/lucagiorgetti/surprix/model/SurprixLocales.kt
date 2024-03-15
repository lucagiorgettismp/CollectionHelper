package com.lucagiorgetti.surprix.model

import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication

/**
 * Created by Luca on 06/11/2017.
 */
enum class SurprixLocales {
    NORTH_AMERICA,
    SOUTH_AMERICA,
    EUROPE,
    ASIA,
    AFRICA,
    AUSTRALIA,
    WORLD_WIDE,
    ITALIA,
    FRANCIA,
    GERMANIA,
    AUSTRIA,
    UK,
    PORTOGALLO,
    SVIZZERA,
    SPAGNA,
    POLONIA,
    RUSSIA,
    CINA,
    GIAPPONE,
    LIBANO,
    EGITTO,
    INDIA,
    USA,
    CANADA,
    ARGENTINA,
    BRASILE,
    MESSICO,
    SUD_AFRICA;

    companion object {
        fun getCode(locale: SurprixLocales?): String {
            when (locale) {
                NORTH_AMERICA -> return "noam"
                SOUTH_AMERICA -> return "soam"
                EUROPE -> return "eur"
                ASIA -> return "asi"
                AFRICA -> return "afr"
                AUSTRALIA -> return "au"
                WORLD_WIDE -> return "wrld"
                ITALIA -> return "it"
                FRANCIA -> return "fr"
                GERMANIA -> return "de"
                AUSTRIA -> return "at"
                UK -> return "uk"
                PORTOGALLO -> return "pt"
                SVIZZERA -> return "ch"
                SPAGNA -> return "sp"
                POLONIA -> return "pl"
                RUSSIA -> return "ru"
                CINA -> return "cn"
                GIAPPONE -> return "jp"
                LIBANO -> return "lb"
                EGITTO -> return "eg"
                INDIA -> return "in"
                USA -> return "us"
                CANADA -> return "ca"
                ARGENTINA -> return "ar"
                BRASILE -> return "br"
                MESSICO -> return "mx"
                SUD_AFRICA -> return "za"
                else -> {}
            }
            return ""
        }

        fun getDisplayName(code: String?): String {
            var stringId = -1
            when (code) {
                "noam" -> stringId = R.string.north_america
                "soam" -> stringId = R.string.south_america
                "eur" -> stringId = R.string.europe
                "asi" -> stringId = R.string.asia
                "afr" -> stringId = R.string.africa
                "au" -> stringId = R.string.australia
                "wrld" -> stringId = R.string.everywhere
                "it" -> stringId = R.string.italia
                "fr" -> stringId = R.string.francia
                "de" -> stringId = R.string.germania
                "at" -> stringId = R.string.austria
                "uk" -> stringId = R.string.united_kingdom
                "pt" -> stringId = R.string.portogallo
                "ch" -> stringId = R.string.svizzera
                "sp" -> stringId = R.string.spagna
                "pl" -> stringId = R.string.polonia
                "ru" -> stringId = R.string.russia
                "cn" -> stringId = R.string.cina
                "jp" -> stringId = R.string.giappone
                "lb" -> stringId = R.string.libano
                "eg" -> stringId = R.string.egitto
                "in" -> stringId = R.string.india
                "us" -> stringId = R.string.usa
                "ca" -> stringId = R.string.canada
                "ar" -> stringId = R.string.argentina
                "br" -> stringId = R.string.brasile
                "mx" -> stringId = R.string.messico
                "za" -> stringId = R.string.sud_africa
            }
            return SurprixApplication.instance.getString(stringId)
        }

        fun getByCode(code: String?): SurprixLocales? {
            when (code) {
                "noam" -> return NORTH_AMERICA
                "soam" -> return SOUTH_AMERICA
                "eur" -> return EUROPE
                "asi" -> return ASIA
                "afr" -> return AFRICA
                "au" -> return AUSTRALIA
                "wrld" -> return WORLD_WIDE
                "it" -> return ITALIA
                "fr" -> return FRANCIA
                "de" -> return GERMANIA
                "at" -> return AUSTRIA
                "uk" -> return UK
                "pt" -> return PORTOGALLO
                "ch" -> return SVIZZERA
                "sp" -> return SPAGNA
                "pl" -> return POLONIA
                "ru" -> return RUSSIA
                "cn" -> return CINA
                "jp" -> return GIAPPONE
                "lb" -> return LIBANO
                "eg" -> return EGITTO
                "in" -> return INDIA
                "us" -> return USA
                "ca" -> return CANADA
                "ar" -> return ARGENTINA
                "br" -> return BRASILE
                "mx" -> return MESSICO
                "za" -> return SUD_AFRICA
            }
            return null
        }
    }
}