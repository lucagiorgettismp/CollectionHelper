package com.lucagiorgetti.collectionhelper.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Year {
    private String id;
    private int year = -1;
    private String producer_id = null;
    private String producer_color = null;

    public Year(int year, Producer producer) {
        this.year = year;
        this.producer_id = producer.getId();
        this.producer_color = producer.getColor();
        this.id = producer.getId() + "_" + String.valueOf(year);
    }

    public Year (){

    }

    public int getYear() {
        return year;
    }

    public String getProducerId() {
        return producer_id;
    }

    public String getId() {
        return id;
    }

    public String getProducer_color() {
        return producer_color;
    }
}