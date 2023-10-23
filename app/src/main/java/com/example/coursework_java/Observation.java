package com.example.coursework_java;

public class Observation {
    private String name;
    private String desc;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Observation(String name, String desc, String imageUrl) {
        this.name = name;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }
}
