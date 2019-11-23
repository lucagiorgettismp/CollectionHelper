package com.lucagiorgetti.surprix.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class MissingSurprise{
    private Surprise surprise;
    private MissingDetail detail;

    public Surprise getSurprise() {
        return surprise;
    }

    public void setSurprise(Surprise surprise) {
        this.surprise = surprise;
    }

    public MissingDetail getDetail() {
        return detail;
    }

    public void setDetail(MissingDetail detail) {
        this.detail = detail;
    }
}


