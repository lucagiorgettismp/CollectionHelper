package com.lucagiorgetti.surprix.model;

public class MissingSurprise implements Comparable<MissingSurprise> {
    Surprise surprise;
    //String notes = "";

    public MissingSurprise(Surprise surprise, String notes) {
        this.surprise = surprise;
        //this.notes = notes;
    }

    public MissingSurprise(Surprise surprise) {
        this.surprise = surprise;
    }

    /*
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
     */
    public Surprise getSurprise() {
        return surprise;
    }

    @Override
    public int compareTo(MissingSurprise missingSurprise) {
        return surprise.compareTo(missingSurprise.surprise);
    }
}
