package com.example.student.lifecyclesdemo;

public class ImageUpload {

    public String name;
    public String url;

    public ImageUpload() {
        // Required empty public constructor
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ImageUpload(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
