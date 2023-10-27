package com.example.coursework_java;

public class Observation {
    private String name;
    private String desc;
    private String imageUri;
    private String time;

    private byte[] imageData;

    private int hikeId;
    private int key;

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public String getImageUri() {
        return imageUri;
    }
    public String getTime() { return time; }
    public byte[] getImageData() {
        return imageData;
    }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Observation(String name, String desc, String time, String imageUri) {
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.imageUri = imageUri;
    }

    public Observation(String name, String desc, String time, byte[] imageData) {
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.imageData = imageData;
    }

    public Observation() {

    }
}
