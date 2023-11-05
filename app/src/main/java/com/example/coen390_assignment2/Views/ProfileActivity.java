package com.example.coen390_assignment2.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coen390_assignment2.Controllers.AccessDBHelper;
import com.example.coen390_assignment2.Models.Access;
import com.example.coen390_assignment2.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    protected Long profileId;

    protected String surname, name, creationDate;

    protected Float gpa;

    protected TextView surnameTextView, nameTextView, IDTextView, GPATextView, ProfileCreatedTextView;

    protected AccessDBHelper dbHelper;

    protected Toolbar toolbar;

    protected ListView accessListView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new AccessDBHelper(getApplicationContext());

        surnameTextView = findViewById(R.id.surnameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        IDTextView = findViewById(R.id.IDTextView);
        GPATextView = findViewById(R.id.GPATextView);
        ProfileCreatedTextView = findViewById(R.id.ProfileCreatedTextView);
        accessListView = findViewById(R.id.accessListView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Profile Activity");

        // Retrieve the extra data from the Intent
        Intent intent = getIntent();

        surname = intent.getStringExtra("surname");
        name = intent.getStringExtra("name");
        profileId = intent.getLongExtra("profileId", -1);
        gpa = intent.getFloatExtra("gpa", -1);
        creationDate = intent.getStringExtra("dateCreated");

        surnameTextView.setText(surname);
        nameTextView.setText(name);
        IDTextView.setText(Long.toString(profileId));
        GPATextView.setText(Float.toString(gpa));
        ProfileCreatedTextView.setText(creationDate);

        if (profileId != -1 || gpa != -1){
            List<Access> accessList = dbHelper.getAccessFromProfileID(profileId, getApplicationContext());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accessListFromProfileIDToString(accessList));

            accessListView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Invalid Profile ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected String[] accessListFromProfileIDToString(List<Access> accessList){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd @ HH:mm:ss");

        List<String> formattedAccessStrings = new ArrayList<>();

        for (Access access : accessList) {
            String formattedAccessString = access.getTimestamp().format(formatter) + " " + access.getAccessType().getStringAccessType();
            formattedAccessStrings.add(formattedAccessString);
        }

        // Sort the list of formatted String representations based on the timestamp
        formattedAccessStrings.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Extract the timestamps from the strings and compare them
                LocalDateTime timestamp1 = LocalDateTime.parse(s1.split(" ")[0], formatter);
                LocalDateTime timestamp2 = LocalDateTime.parse(s2.split(" ")[0], formatter);
                return timestamp1.compareTo(timestamp2);
            }
        });

        // Convert the sorted formatted String list back to a String array
        return formattedAccessStrings.toArray(new String[0]);
    }
}