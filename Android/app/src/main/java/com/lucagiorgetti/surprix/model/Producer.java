package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Producer {
    private String id = null;
    private String name = null;
    private String product = null;
    private String color = null;
    private String brand_id = null;
    private int order = 0;

    public Producer(String name, String product, int order, String color) {
        this.id = product != null ? name + "_" + product : name;
        this.name = product != null ? name + " " + product : name;
        this.brand_id = name;
        this.product = product;
        this.order = order;
        this.color = color;
    }

    public Producer(String name, int order, String color) {
        this.id = name;
        this.name = name;
        this.brand_id = name;
        this.color = color;
        this.order = order;
    }

    public Producer() {

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getBrandId() {
        return brand_id;
    }
}