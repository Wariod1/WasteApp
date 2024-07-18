package com.example.wasteapp;

public class Schedule {
    private String id;
    private String date;
    private String time;
    private String location;
    private String garbageType;

    public Schedule(String id, String date, String time, String location, String garbageType) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.location = location;
        this.garbageType = garbageType;
    }

    // Getters
    public String getId() { return id; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getGarbageType() { return garbageType; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setLocation(String location) { this.location = location; }
    public void setGarbageType(String garbageType) { this.garbageType = garbageType; }
}
