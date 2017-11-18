package com.lucagiorgetti.collectionhelper.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Year {
    private String id;
    private int year = -1;
    private String producerId = null;

    public Year(int year, Producer producer) {
        this.year = year;
        this.producerId = producer.getId();
        this.id = producer.getId() + "_" + String.valueOf(year);
    }

    public Year (){

    }

    public int getYear() {
        return year;
    }

    public String getProducerId() {
        return producerId;
    }

    public String getId() {
        return id;
    }
}