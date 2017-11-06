package com.lucagiorgetti.collectionhelper.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Product {
    private String name;
    private Producer producer;

    public Product(String name, Producer producer) {
        this.name = name;
        this.producer = producer;
    }

    public String getName() {
        return name;
    }

    public Producer getProducer() {
        return producer;
    }
}