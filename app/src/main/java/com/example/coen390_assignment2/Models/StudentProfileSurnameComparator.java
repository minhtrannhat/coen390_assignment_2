package com.example.coen390_assignment2.Models;

import java.util.Comparator;

public class StudentProfileSurnameComparator implements Comparator<StudentProfile> {
    @Override
    public int compare(StudentProfile profile1, StudentProfile profile2) {
        return profile1.getSurname().compareTo(profile2.getSurname());
    }
}
