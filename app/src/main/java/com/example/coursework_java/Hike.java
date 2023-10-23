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
    private List<Observation> observations;
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void addObservation(Observation observation) {
        observations.add(observation);
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

    public List<Observation> getObservations() {
        return observations;
    }

    public Hike(String hikeName, String hikeLocation, String hikeDesc, String hikeDate, String hikeLevel, String hikeLength, int hasParking) {
        this.hikeName = hikeName;
        this.hikeLocation = hikeLocation;
        this.hikeDesc = hikeDesc;
        this.hikeDate = hikeDate;
        this.hikeLevel = hikeLevel;
        this.hikeLength = hikeLength;
        this.hasParking = hasParking;
        this.observations = new ArrayList<>();
    }

    public Hike() {

    }
}
