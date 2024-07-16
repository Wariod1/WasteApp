package com.example.wasteapp.person;

public class Person {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String location;

    public Person(String id, String fullName, String phoneNumber, String location) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.location = location;
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
}