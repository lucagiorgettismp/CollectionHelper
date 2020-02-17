package com.lucagiorgetti.surprix.model;

import java.util.Comparator;

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

    public static class SortByCode implements Comparator<MissingSurprise> {
        @Override
        public int compare(MissingSurprise o1, MissingSurprise o2) {
            return o1.getSurprise().getCode().compareTo(o2.getSurprise().getCode());
        }
    }
}


