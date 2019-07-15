package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Producer {
    private String id = null;
    private String name = null;
    private String product = null;
    private String color = null;
    private int order = 0;

    public Producer(String name, String product, int order, String color) {
        this.id = name + "_" + product;
        this.name = name;
        this.product = product;
        this.order = order;
        this.color = color;
    }

    public Producer(String name, int order, String color) {
        this.id = name;
        this.name = name;
        this.color = color;
        this.order = order;
    }

    public Producer() {

    }

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
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
}