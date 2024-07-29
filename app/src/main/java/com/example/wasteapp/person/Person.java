package com.example.wasteapp.person;

public class Person {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String location;
    private String wasteType;
    private String acceptedDateTime; // New field

    public Person(String id, String fullName, String phoneNumber, String location, String wasteType) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.wasteType = wasteType;
        this.acceptedDateTime = null; // Initialize as null
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getWasteType() {
        return wasteType;
    }

    public String getAcceptedDateTime() {
        return acceptedDateTime;
    }

    public void setAcceptedDateTime(String acceptedDateTime) {
        this.acceptedDateTime = acceptedDateTime;
    }
}