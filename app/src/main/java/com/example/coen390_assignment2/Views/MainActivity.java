package com.example.coen390_assignment2.Views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coen390_assignment2.Controllers.StudentProfileDBHelper;
import com.example.coen390_assignment2.Models.StudentProfile;
import com.example.coen390_assignment2.Models.StudentProfileIDComparator;
import com.example.coen390_assignment2.Models.StudentProfileSurnameComparator;
import com.example.coen390_assignment2.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected StudentProfileDBHelper studentProfileDBHelper;

    protected Toolbar toolbar;
    protected Button showDialogButton;

    protected ListView studentProfileList;

    protected boolean profileNameDisplayMode;

    protected MenuItem toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        showDialogButton = findViewById(R.id.add_profile_action_button);
        studentProfileList = findViewById(R.id.profilesListView);

        // Initialize the ActionBar (Toolbar)
        setSupportActionBar(toolbar);

        studentProfileDBHelper = new StudentProfileDBHelper(getApplicationContext());

        initAddProfileActionButton(showDialogButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // surname when true, ID when false
        profileNameDisplayMode = true;

        List<StudentProfile> studentProfiles = studentProfileDBHelper.getAllStudentProfile(getApplicationContext());

        // by default sort by surname
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, showStudentProfileList(studentProfiles, profileNameDisplayMode));

        // set tool bar subtitle text
        toolbar.setSubtitle(studentProfiles.size() + " Profiles, by " + (profileNameDisplayMode ? "Surname" : "ID") );

        studentProfileList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_main_activity, menu);
        toggle = menu.findItem(R.id.toggle_profiles_display_mode);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // toggle to the next mode
        if (profileNameDisplayMode){
            toggle.setTitle("By ID");
        } else{
            toggle.setTitle("By Surname");
        }
        return true;
    }

    protected String[] showStudentProfileList(List<StudentProfile> studentProfiles, boolean profileNameDisplayMode){
        // Create a new list for sorted profiles
        List<StudentProfile> sortedProfiles = new ArrayList<>(studentProfiles);

        // Create a String array to hold the formatted strings
        String[] profileListToString = new String[sortedProfiles.size()];

        // sort by surname
        if (profileNameDisplayMode){
            // Sort the new list based on the "surname" field
            sortedProfiles.sort(new StudentProfileSurnameComparator());

            for (int i = 0; i < sortedProfiles.size(); i++) {
                StudentProfile profile = sortedProfiles.get(i);
                String formattedString = i + ", " + profile.getSurname() + ", " + profile.getName();
                profileListToString[i] = formattedString;
            }
        }

        // sort by ID
        else {
            sortedProfiles.sort(new StudentProfileIDComparator());

            for (int i = 0; i < sortedProfiles.size(); i++) {
                StudentProfile profile = sortedProfiles.get(i);
                String formattedString = i + ", " + profile.getProfileID();
                profileListToString[i] = formattedString;
            }
        }

        return profileListToString;
    }

    protected void initAddProfileActionButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DialogFragment
                InsertProfileDialogFragment dialogFragment = new InsertProfileDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "dialog_fragment_tag");
            }
        });
    }
}