package com.example.coen390_assignment2.Controllers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.coen390_assignment2.Models.Access;
import com.example.coen390_assignment2.Models.AccessContract;
import com.example.coen390_assignment2.Models.AccessType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccessDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private Context context = null;

    public AccessDBHelper(@Nullable Context context) {
        super(context, AccessContract.AccessEntry.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table
        String CREATE_COURSE_TABLE = "CREATE TABLE " + AccessContract.AccessEntry.TABLE_NAME + " (" +
                AccessContract.AccessEntry.COLUMN_NAME_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AccessContract.AccessEntry.COLUMN_NAME_PROFILE_ID + " INTEGER NOT NULL, " +
                AccessContract.AccessEntry.COLUMN_NAME_ACCESS_TYPE + " TEXT NOT NULL, " +
                AccessContract.AccessEntry.COLUMN_NAME_TIMESTAMP + " TEXT NOT NULL);";

        db.execSQL(CREATE_COURSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + AccessContract.AccessEntry.DATABASE_NAME);
        onCreate(db);
    }

    public long insertAccess(Access access) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AccessContract.AccessEntry.COLUMN_NAME_PROFILE_ID, access.getProfileID());
        contentValues.put(AccessContract.AccessEntry.COLUMN_NAME_ACCESS_TYPE, access.getAccessType().getStringAccessType());
        contentValues.put(AccessContract.AccessEntry.COLUMN_NAME_TIMESTAMP, access.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        try {
            id = db.insertOrThrow(AccessContract.AccessEntry.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Toast.makeText(context, "DB insert failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return id;
    }

    public List<Access> getAccessFromProfileID(long profileID) {
        List<Access> accessList = new ArrayList<>();

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = null;

            String[] columns = {"AccessId", "ProfileID", "AccessType", "Timestamp"};
            String selection = "ProfileID = ?";
            String[] selectionArgs = {String.valueOf(profileID)};
            String orderBy = "Timestamp";

            cursor = db.query(AccessContract.AccessEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") int accessId = cursor.getInt(cursor.getColumnIndex(AccessContract.AccessEntry.COLUMN_NAME_ACCESS_ID));
                        @SuppressLint("Range") int _profileID = cursor.getInt(cursor.getColumnIndex(AccessContract.AccessEntry.COLUMN_NAME_PROFILE_ID));
                        @SuppressLint("Range") String accessType = cursor.getString(cursor.getColumnIndex("AccessType"));
                        @SuppressLint("Range") String timestampStr = cursor.getString(cursor.getColumnIndex("Timestamp"));

                        // Parse the timestamp string into a LocalDateTime object
                        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                        Access access = new Access(accessId, _profileID, AccessType.matchEnum(accessType), timestamp);
                        accessList.add(access);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(context, "DB get failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return accessList;
    }
}
