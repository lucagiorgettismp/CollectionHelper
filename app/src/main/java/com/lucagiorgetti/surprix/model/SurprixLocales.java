package com.lucagiorgetti.surprix.model;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;

/**
 * Created by Luca on 06/11/2017.
 */
public enum SurprixLocales {
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

    public static String getCode(SurprixLocales locale) {
        switch (locale) {
            case NORTH_AMERICA:
                return "noam";
            case SOUTH_AMERICA:
                return "soam";
            case EUROPE:
                return "eur";
            case ASIA:
                return "asi";
            case AFRICA:
                return "afr";
            case AUSTRALIA:
                return "au";
            case WORLD_WIDE:
                return "wrld";
            case ITALIA:
                return "it";
            case FRANCIA:
                return "fr";
            case GERMANIA:
                return "de";
            case AUSTRIA:
                return "at";
            case UK:
                return "uk";
            case PORTOGALLO:
                return "pt";
            case SVIZZERA:
                return "ch";
            case SPAGNA:
                return "sp";
            case POLONIA:
                return "pl";
            case RUSSIA:
                return "ru";
            case CINA:
                return "cn";
            case GIAPPONE:
                return "jp";
            case LIBANO:
                return "lb";
            case EGITTO:
                return "eg";
            case INDIA:
                return "in";
            case USA:
                return "us";
            case CANADA:
                return "ca";
            case ARGENTINA:
                return "ar";
            case BRASILE:
                return "br";
            case MESSICO:
                return "mx";
            case SUD_AFRICA:
                return "za";
        }

        return "";
    }

    public static String getDisplayName(String code) {
        int stringId = -1;
        switch (code) {
            case "noam":
                stringId = R.string.north_america;
                break;
            case "soam":
                stringId = R.string.south_america;
                break;
            case "eur":
                stringId = R.string.europe;
                break;
            case "asi":
                stringId = R.string.asia;
                break;
            case "afr":
                stringId = R.string.africa;
                break;
            case "au":
                stringId = R.string.australia;
                break;
            case "wrld":
                stringId = R.string.everywhere;
                break;
            case "it":
                stringId = R.string.italia;
                break;
            case "fr":
                stringId = R.string.francia;
                break;
            case "de":
                stringId = R.string.germania;
                break;
            case "at":
                stringId = R.string.austria;
                break;
            case "uk":
                stringId = R.string.united_kingdom;
                break;
            case "pt":
                stringId = R.string.portogallo;
                break;
            case "ch":
                stringId = R.string.svizzera;
                break;
            case "sp":
                stringId = R.string.spagna;
                break;
            case "pl":
                stringId = R.string.polonia;
                break;
            case "ru":
                stringId = R.string.russia;
                break;
            case "cn":
                stringId = R.string.cina;
                break;
            case "jp":
                stringId = R.string.giappone;
                break;
            case "lb":
                stringId = R.string.libano;
                break;
            case "eg":
                stringId = R.string.egitto;
                break;
            case "in":
                stringId = R.string.india;
                break;
            case "us":
                stringId = R.string.usa;
                break;
            case "ca":
                stringId = R.string.canada;
                break;
            case "ar":
                stringId = R.string.argentina;
                break;
            case "br":
                stringId = R.string.brasile;
                break;
            case "mx":
                stringId = R.string.messico;
                break;
            case "za":
                stringId = R.string.sud_africa;
                break;
        }

        return SurprixApplication.getInstance().getString(stringId);
    }

    public static SurprixLocales getByCode(String code) {
        switch (code) {
            case "noam":
                return NORTH_AMERICA;
            case "soam":
                return SOUTH_AMERICA;
            case "eur":
                return EUROPE;
            case "asi":
                return ASIA;
            case "afr":
                return AFRICA;
            case "au":
                return AUSTRALIA;
            case "wrld":
                return WORLD_WIDE;
            case "it":
                return ITALIA;
            case "fr":
                return FRANCIA;
            case "de":
                return GERMANIA;
            case "at":
                return AUSTRIA;
            case "uk":
                return UK;
            case "pt":
                return PORTOGALLO;
            case "ch":
                return SVIZZERA;
            case "sp":
                return SPAGNA;
            case "pl":
                return POLONIA;
            case "ru":
                return RUSSIA;
            case "cn":
                return CINA;
            case "jp":
                return GIAPPONE;
            case "lb":
                return LIBANO;
            case "eg":
                return EGITTO;
            case "in":
                return INDIA;
            case "us":
                return USA;
            case "ca":
                return CANADA;
            case "ar":
                return ARGENTINA;
            case "br":
                return BRASILE;
            case "mx":
                return MESSICO;
            case "za":
                return SUD_AFRICA;
        }

        return null;
    }
}