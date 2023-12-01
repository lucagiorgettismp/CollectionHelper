package com.lucagiorgetti.surprix.model;

import java.util.Comparator;

/**
 * Created by Utente on 17/04/2017.
 */

public class Set {
    private String id = null;
    private String code = null;
    private String name = null;
    private String year_id = null;
    private String year_desc = null;
    private int year_year = -1;
    private String producer_id = null;
    private String producer_name = null;
    private String producer_color = null;
    private String nation = null;
    private String img_path = null;
    private String category = null;
    private boolean effective_code = true;


    public Set(String name, String code, Year year, Producer producer, String nation, String img_path, String category) {
        this.name = name;
        this.code = code;
        this.producer_id = producer.getId();
        this.producer_name = producer.getName();
        this.producer_color = producer.getColor();
        this.year_id = year.getId();
        this.year_desc = year.getDescr();
        this.year_year = year.getYear();
        this.nation = nation;
        this.img_path = img_path;
        this.category = category;
        this.id = producer.getName() + "_" + year.getYear() + "_" + code;
    }

    public Set(String name, String code, Year year, Producer producer, String nation, String img_path, String category, boolean effectiveCode) {
        this.name = name;
        this.code = code;
        this.producer_id = producer.getId();
        this.producer_name = producer.getName();
        this.producer_color = producer.getColor();
        this.year_id = year.getId();
        this.year_desc = year.getDescr();
        this.year_year = year.getYear();
        this.nation = nation;
        this.img_path = img_path;
        this.category = category;
        this.id = producer.getName() + "_" + year.getYear() + "_" + code;
        this.effective_code = effectiveCode;
    }


    public Set() {
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getNation() {
        return nation;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getId() {
        return id;
    }

    public String getProducer_name() {
        return producer_name;
    }

    public String getYear_id() {
        return year_id;
    }

    public String getProducer_color() {
        return producer_color;
    }

    public String getCategory() {
        return category;
    }

    public String getYear_desc() {
        return year_desc;
    }

    public int getYear_year() {
        return year_year;
    }

    public String getProducer_id() {
        return producer_id;
    }

    public boolean isEffectiveCode() {
        return effective_code;
    }
}