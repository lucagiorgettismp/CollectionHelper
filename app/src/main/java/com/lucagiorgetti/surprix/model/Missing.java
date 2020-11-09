package com.lucagiorgetti.surprix.model;

import java.util.Comparator;

/**
 * Created by Utente on 17/04/2017.
 */

public class Missing {
    private String notes;
    private String id;

    public Missing(String surpriseId, String notes) {
        this.notes = notes;
        this.id = surpriseId;
    }

    public Missing(String surpriseId) {
        this.notes = "";
        this.id = surpriseId;
    }

    public Missing( ) {

    }

    public String getNotes() {
        return notes;
    }

    public String getId() {
        return id;
    }
}


