package com.example.bg_rem.act.model;

import java.io.Serializable;

public class Image {
    private String title;
    private String image;

    public Image() {
    }

    public Image(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = title;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
