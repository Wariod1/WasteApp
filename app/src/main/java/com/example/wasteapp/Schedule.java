package com.example.wasteapp;

public class Schedule {
    private String date;
    private String time;
    private String location;
    private String wasteType;

    public Schedule(String date, String time, String location, String wasteType) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.wasteType = wasteType;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getWasteType() {
        return wasteType;
    }
}
