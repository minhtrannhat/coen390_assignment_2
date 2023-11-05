package com.example.coen390_assignment2.Models;

import java.util.Comparator;

public class StudentProfileIDComparator implements Comparator<StudentProfile> {
    @Override
    public int compare(StudentProfile profile1, StudentProfile profile2) {
        return Long.compare(profile1.getProfileID(), profile2.getProfileID());
    }
}
