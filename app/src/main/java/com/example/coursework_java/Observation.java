package com.example.coursework_java;

public class Observation {
    private String name;
    private String desc;
    private String imageUrl;
    private String time;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getTime() { return time; }

    public Observation(String name, String desc, String time, String imageUrl) {
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.imageUrl = imageUrl;
    }
}
