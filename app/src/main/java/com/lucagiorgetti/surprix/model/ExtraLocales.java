package com.lucagiorgetti.surprix.model;

/**
 * Created by Luca on 06/11/2017.
 */

public final class ExtraLocales {
    private static final String NORTH_AMERICA = "noam";
    private static final String SOUTH_AMERICA = "soam";
    public static final String EUROPE = "eur";
    private static final String ASIA = "asi";
    private static final String AFRICA = "afr";
    private static final String OCEANIA = "oce";
    private static final String WORLD_WIDE = "wrld";

    private ExtraLocales() {
    }

    public static boolean isExtraLocale(String set_nation) {
        return (set_nation.equals(NORTH_AMERICA) || set_nation.equals(SOUTH_AMERICA) || set_nation.equals(EUROPE) ||
                set_nation.equals(ASIA) || set_nation.equals(AFRICA) || set_nation.equals(OCEANIA) || set_nation.equals(WORLD_WIDE));
    }

    public static String getDisplayName(String set_nation) {
        String name = null;
        switch (set_nation) {
            case NORTH_AMERICA:
                name = "America Nord";
                break;
            case SOUTH_AMERICA:
                name = "America Sud";
                break;
            case EUROPE:
                name = "Europa";
                break;
            case ASIA:
                name = "Asia";
                break;
            case AFRICA:
                name = "Africa";
                break;
            case OCEANIA:
                name = "Oceania";
                break;
            case WORLD_WIDE:
                name = "Ovunque";
                break;
        }
        return name;
    }
}
