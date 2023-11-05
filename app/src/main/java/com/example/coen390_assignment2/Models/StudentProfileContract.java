package com.example.coen390_assignment2.Models;

import android.provider.BaseColumns;

public final class StudentProfileContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private StudentProfileContract() {
    }

    /* Inner class that defines the table contents */
    public static class StudentProfileEntry implements BaseColumns {
        public static final String DATABASE_NAME = "student-profile-db";
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME_PROFILE_ID = "profile-id";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PROFILE_GPA = "profile-gpa";
        public static final String COLUMN_NAME_PROFILE_CREATION_DATE = "profile-creation-date";
    }
}
