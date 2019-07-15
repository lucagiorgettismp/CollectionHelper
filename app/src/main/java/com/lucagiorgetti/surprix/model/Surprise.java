package com.lucagiorgetti.surprix.model;

import android.support.annotation.Nullable;

import java.util.Comparator;

/**
 * Created by Utente on 17/04/2017.
 */

public class Surprise {
    private String id = null;
    private String description = null;
    private String img_path = null;
    private String code = null;
    private String set_name = null;
    private int set_year = -1;
    private String set_producer_name = null;
    private String set_product_name = null;
    private String set_producer_color = null;
    private String set_nation = null;
    private String set_id = null;
    private String rarity = null;

    public Surprise() {
    }

    public Surprise(String description, String img_path, String code, Set set, @Nullable Integer rarity) {
        this.description = description;
        this.img_path = img_path;
        this.code = code;
        this.set_name = set.getName();
        this.set_year = set.getYear();
        this.set_producer_color = set.getProducer_color();
        this.set_producer_name = set.getProducer_name();
        this.set_product_name = set.getProduct();
        this.set_nation = set.getNation();
        this.id = set_producer_name + "_" + set_year + "_" + set.getCode() + "_" + code;
        this.set_id = set.getId();
        this.rarity = rarity != null ? String.valueOf(rarity) : null;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getCode() {
        return code;
    }

    public String getSet_name() {
        return set_name;
    }

    public int getSet_year() {
        return set_year;
    }

    public String getSet_producer_name() {
        return set_producer_name;
    }

    public String getSet_product_name() {
        return set_product_name;
    }

    public String getSet_nation() {
        return set_nation;
    }

    public String getSet_id() {
        return set_id;
    }

    public String getSet_producer_color() {
        return set_producer_color;
    }

    public static class SortByCode implements Comparator<Surprise> {
        @Override
        public int compare(Surprise o1, Surprise o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }

    public String getRarity() {
        return rarity;
    }

    public Integer getIntRarity() {
        return rarity != null ? Integer.valueOf(rarity) : null;
    }
}


