package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class MissingDetail {
    private String id = null;
    private String notes = null;

    public MissingDetail(String id, String note) {
        this.id = id;
        this.notes = notes;
    }

    public MissingDetail() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}