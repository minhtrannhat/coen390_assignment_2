package com.example.coen390_assignment2.Models;

import java.time.LocalDate;

public class StudentProfile {
    private long profileID;
    private String name;
    private String surname;
    private float gpa;
    private final LocalDate profileCreationDate;

    public StudentProfile(String surname, String name, long profileID, float gpa, LocalDate profileCreationDate) {
        this.surname = surname;
        this.name = name;
        this.profileID = profileID;
        this.gpa = gpa;
        this.profileCreationDate = profileCreationDate;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProfileID() {
        return this.profileID;
    }

    public void setProfileID(long profileID) {
        this.profileID = profileID;
    }

    public float getGPA(){
        return this.gpa;
    }

    public void setGPA(float gpa){
        this.gpa = gpa;
    }

    public LocalDate getProfileCreationDate() {
        return profileCreationDate;
    }
}