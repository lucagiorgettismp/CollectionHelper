package com.lucagiorgetti.surprix.model;

import androidx.annotation.Nullable;

/**
 * Created by Utente on 17/04/2017.
 */

public class Surprise implements Comparable<Surprise>{
    private String id = null;
    private String description = null;
    private String img_path = null;
    private String code = null;
    private String set_name = null;
    private int set_year_year = -1;
    private String set_year_id = null;
    private String set_year_name = null;
    private String set_producer_name = null;
    private String set_producer_id = null;
    private String set_producer_color = null;
    private String set_nation = null;
    private String set_category = null;
    private String set_id = null;
    private String rarity = null;

    private boolean set_effective_code = true;

    public Surprise(){
    }

    public Surprise(String description, String img_path, String code, Set set, @Nullable Integer rarity) {
        this.description = description;
        this.img_path = img_path;
        this.code = code;
        this.set_name = set.getName();
        this.set_producer_color = set.getProducer_color();
        this.set_producer_name = set.getProducer_name();
        this.set_producer_id = set.getProducer_id();
        this.set_effective_code = set.isEffectiveCode();
        this.set_category = set.getCategory();
        this.set_nation = set.getNation();
        this.set_year_year = set.getYear_year();
        this.set_year_id = set.getYear_id();
        this.set_year_name = set.getYear_desc();
        this.id = set_producer_name + "_" + set_year_year + "_" + set.getCode() + "_" + code;
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

    public String getSet_producer_name() {
        return set_producer_name;
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

    public String getRarity(){
        return rarity;
    }


    public Integer getIntRarity(){
        return rarity != null ? Integer.parseInt(this.getRarity()) : null;
    }

    public boolean isSet_effective_code() {
        return set_effective_code;
    }

    public int getSet_year_year() {
        return set_year_year;
    }

    public String getSet_year_id() {
        return set_year_id;
    }

    public String getSet_year_name() {
        return set_year_name;
    }

    public String getSet_producer_id() {
        return set_producer_id;
    }

    public String getSet_category() {
        return set_category;
    }

    public void setSet_name(String set_name) {
        this.set_name = set_name;
    }

    public void setSet_year_year(int set_year_year) {
        this.set_year_year = set_year_year;
    }

    public void setSet_year_id(String set_year_id) {
        this.set_year_id = set_year_id;
    }

    public void setSet_year_name(String set_year_name) {
        this.set_year_name = set_year_name;
    }

    public void setSet_producer_name(String set_producer_name) {
        this.set_producer_name = set_producer_name;
    }

    public void setSet_producer_id(String set_producer_id) {
        this.set_producer_id = set_producer_id;
    }

    public void setSet_producer_color(String set_producer_color) {
        this.set_producer_color = set_producer_color;
    }

    public void setSet_nation(String set_nation) {
        this.set_nation = set_nation;
    }

    public void setSet_category(String set_category) {
        this.set_category = set_category;
    }

    @Override
    public int compareTo(Surprise surprise) {
        try {
            int a = Integer.parseInt(this.getCode());
            int b = Integer.parseInt(surprise.getCode());
            return a - b;
        } catch (Exception ignored) {

        }
        return getCode().compareTo(surprise.getCode());
    }
}





