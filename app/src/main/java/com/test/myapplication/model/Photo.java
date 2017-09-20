package com.test.myapplication.model;

/**
 * Created by Max on 20.09.2017.
 */

public class Photo {

    private String url;
    private String text;

    public Photo(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
