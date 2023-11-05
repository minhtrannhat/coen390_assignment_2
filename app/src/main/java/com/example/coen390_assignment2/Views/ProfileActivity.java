package com.example.coen390_assignment2.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coen390_assignment2.Controllers.AccessDBHelper;
import com.example.coen390_assignment2.Models.Access;
import com.example.coen390_assignment2.Models.AccessType;
import com.example.coen390_assignment2.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("ProfileActivity", "handleOnBackPressed: Pressed");
                createAccessClosed(profileId, AccessType.CLOSED, LocalDateTime.now());
                finish();
            }
        };

        // Add the callback to the back button dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

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

        if (profileId != -1 || gpa != -1) {
            List<Access> accessList = dbHelper.getAccessFromProfileID(profileId, getApplicationContext());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accessListFromProfileIDToString(accessList));

            accessListView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Invalid Profile ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            createAccessClosed(profileId, AccessType.CLOSED, LocalDateTime.now());
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    protected String[] accessListFromProfileIDToString(List<Access> accessList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd @ HH:mm:ss");

        List<String> formattedAccessStrings = new ArrayList<>();

        for (Access access : accessList) {
            String formattedAccessString = access.getTimestamp().format(formatter) + " " + access.getAccessType().getStringAccessType();
            formattedAccessStrings.add(formattedAccessString);
        }

        // Convert the sorted formatted String list back to a String array
        return formattedAccessStrings.toArray(new String[0]);
    }

    protected void createAccessClosed(long profileId, AccessType accessType, LocalDateTime timestamp) {
        Access access = new Access(profileId, accessType, timestamp);
        dbHelper.insertAccess(access, getApplicationContext());
    }
}