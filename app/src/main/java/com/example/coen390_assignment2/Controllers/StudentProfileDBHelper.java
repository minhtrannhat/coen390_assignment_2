package com.example.coen390_assignment2.Controllers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coen390_assignment2.Models.StudentProfile;
import com.example.coen390_assignment2.Models.StudentProfileContract;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentProfileDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private Context context = null;

    public StudentProfileDBHelper(@Nullable Context context) {
        super(context, StudentProfileContract.StudentProfileEntry.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table
        String CREATE_COURSE_TABLE = "CREATE TABLE " + StudentProfileContract.StudentProfileEntry.TABLE_NAME + " (" + StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_ID + " INTEGER PRIMARY KEY, " + StudentProfileContract.StudentProfileEntry.COLUMN_NAME_SURNAME + " TEXT NOT NULL, " + StudentProfileContract.StudentProfileEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, " + StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_GPA + " REAL NOT NULL, " + StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_CREATION_DATE + " TEXT NOT NULL);";

        db.execSQL(CREATE_COURSE_TABLE);
        Log.d("StudentProfileDBHelper", "onCreate: Created course table" + CREATE_COURSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + StudentProfileContract.StudentProfileEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insertStudentProfile(StudentProfile studentProfile, Context context) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_ID, studentProfile.getProfileID());
        contentValues.put(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_SURNAME, studentProfile.getSurname());
        contentValues.put(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_NAME, studentProfile.getName());
        contentValues.put(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_GPA, studentProfile.getGPA());
        contentValues.put(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_CREATION_DATE, studentProfile.getProfileCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        try {
            // row id
            id = db.insertOrThrow(StudentProfileContract.StudentProfileEntry.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Toast.makeText(context, "Profile ID has to be Unique", Toast.LENGTH_LONG).show();

            Log.e("StudentProfileDBHelper", "insertStudentProfile: " + e);
        } finally {
            db.close();
        }
        return id;
    }

    public List<StudentProfile> getAllStudentProfile(Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<StudentProfile> studentProfiles = new ArrayList<>();

        try {
            cursor = db.query(StudentProfileContract.StudentProfileEntry.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") int profileID = cursor.getInt(cursor.getColumnIndex(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_ID));
                        @SuppressLint("Range") String profileSurname = cursor.getString(cursor.getColumnIndex(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_SURNAME));
                        @SuppressLint("Range") String profileName = cursor.getString(cursor.getColumnIndex(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_NAME));
                        @SuppressLint("Range") float profileGPA = cursor.getFloat(cursor.getColumnIndex(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_GPA));
                        @SuppressLint("Range") String profileCreationDate = cursor.getString(cursor.getColumnIndex(StudentProfileContract.StudentProfileEntry.COLUMN_NAME_PROFILE_CREATION_DATE));

                        StudentProfile studentProfile = new StudentProfile(profileSurname, profileName, profileID, profileGPA, LocalDate.parse(profileCreationDate, DateTimeFormatter.ISO_LOCAL_DATE));

                        studentProfiles.add(studentProfile);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(context, "DB get failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return studentProfiles;
    }
}
