package com.example.wasteapp.person;

public class schedule {
    private int id;
    private int collectorId;
    private int residentId;
    private String date;
    private String time;
    private String status;

    public schedule(int id, int collectorId, int residentId, String date, String time, String status) {
        this.id = id;
        this.collectorId = collectorId;
        this.residentId = residentId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCollectorId() {
        return collectorId;
    }

    public int getResidentId() {
        return residentId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
