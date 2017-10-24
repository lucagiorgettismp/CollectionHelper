package com.lucagiorgetti.collectionhelper.model;

/**
 * Created by Utente on 17/04/2017.
 */

public class Surprise {
    private String description;
    private String img_path;
    private String code;
    private Set set;

    public Surprise(String description, String img_path, String code, Set set) {
        this.description = description;
        this.img_path = img_path;
        this.code = code;
        this.set = set;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getCode() {
        return code;
    }

    public Set getSet() {
        return set;
    }
}


