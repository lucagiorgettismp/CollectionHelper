package com.lucagiorgetti.collectionhelper.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Address {

    private String street_name;
    private int street_number;
    private String city;
    private int CAP;
    private String nation;

    public Address(String street_name, int street_number, String city, int CAP, String nation) {
        this.street_name = street_name;
        this.street_number = street_number;
        this.city = city;
        this.CAP = CAP;
        this.nation = nation;
    }

    public String getStreetName() {
        return street_name;
    }

    public int getStreetNumber() {
        return street_number;
    }

    public String getCity() {
        return city;
    }

    public int getCAP() {
        return CAP;
    }

    public String getNation() {
        return nation;
    }
}