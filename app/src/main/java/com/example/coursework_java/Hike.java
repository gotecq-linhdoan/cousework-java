package com.example.coursework_java;

import java.util.ArrayList;
import java.util.List;

public class Hike {
    private String hikeName;
    private String hikeLocation;
    private String hikeDesc;
    private String hikeDate;
    private String hikeLevel;
    private String hikeLength;
    private int hasParking;
    private List<Observation> observationList;
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getHikeName() {
        return hikeName;
    }

    public String getHikeLocation() {
        return hikeLocation;
    }

    public String getHikeDesc() {
        return hikeDesc;
    }

    public String getHikeDate() {
        return hikeDate;
    }

    public String getHikeLevel() {
        return hikeLevel;
    }

    public String getHikeLength() {
        return hikeLength;
    }

    public int getHasParking() {
        return hasParking;
    }

    public List<Observation> getObservationList() {
        return observationList;
    }

    public void addObservation(Observation observation) {
        observationList.add(observation);
    }

    public Hike(String hikeName, String hikeLocation, String hikeDesc, String hikeDate, String hikeLevel, String hikeLength, int hasParking) {
        this.hikeName = hikeName;
        this.hikeLocation = hikeLocation;
        this.hikeDesc = hikeDesc;
        this.hikeDate = hikeDate;
        this.hikeLevel = hikeLevel;
        this.hikeLength = hikeLength;
        this.hasParking = hasParking;
        this.observationList = new ArrayList<>(); // Initialize the observation list
    }

    public Hike() {

    }
}
