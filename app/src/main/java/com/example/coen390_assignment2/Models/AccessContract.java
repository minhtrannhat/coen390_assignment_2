package com.example.coen390_assignment2.Models;

import android.provider.BaseColumns;

public final class AccessContract {
    private AccessContract(){}

    public static class AccessEntry implements BaseColumns{
        public static final String DATABASE_NAME = "access-entry-db";
        public static final String TABLE_NAME = "access";
        public static final String COLUMN_NAME_ACCESS_ID = "access-id";
        public static final String COLUMN_NAME_PROFILE_ID = "profile-id";
        public static final String COLUMN_NAME_ACCESS_TYPE = "access-type";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
