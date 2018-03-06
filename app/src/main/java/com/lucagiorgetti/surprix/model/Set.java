package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Set {
    private String id = null;
    private String name = null;
    private int year = -1;
    private String year_id = null;
    private String product = null;
    private String producer_name = null;
    private String producer_color = null;
    private String nation = null;
    private String img_path = null;
    private String category = null;

    public Set(String name, Year year, Producer producer, String nation, String img_path, String category) {
        this.name = name;
        this.year = year.getYear();
        this.product = producer.getProduct();
        this.producer_name = producer.getName();
        this.producer_color = producer.getColor();
        this.year_id = year.getId();
        this.nation = nation;
        this.img_path = img_path;
        this.category = category;
        this.id = name.replaceAll("\\s+","") + "_" + year.getYear();
    }

    public Set() {
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getProduct() {
        return product;
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
}