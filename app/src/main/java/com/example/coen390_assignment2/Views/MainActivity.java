package com.example.coen390_assignment2.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coen390_assignment2.Controllers.AccessDBHelper;
import com.example.coen390_assignment2.Controllers.StudentProfileDBHelper;
import com.example.coen390_assignment2.Models.Access;
import com.example.coen390_assignment2.Models.AccessType;
import com.example.coen390_assignment2.Models.StudentProfile;
import com.example.coen390_assignment2.Models.StudentProfileIDComparator;
import com.example.coen390_assignment2.Models.StudentProfileSurnameComparator;
import com.example.coen390_assignment2.R;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected StudentProfileDBHelper studentProfileDBHelper;

    protected AccessDBHelper accessDBHelper;

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

        accessDBHelper = new AccessDBHelper(getApplicationContext());

        initAddProfileActionButton(showDialogButton);

        // surname when true, ID when false
        profileNameDisplayMode = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<StudentProfile> studentProfiles = studentProfileDBHelper.getAllStudentProfile(getApplicationContext());

        // by default sort by surname
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, showStudentProfileList(studentProfiles, profileNameDisplayMode));

        // set tool bar subtitle text
        toolbar.setSubtitle(studentProfiles.size() + " Profiles, by " + (profileNameDisplayMode ? "Surname" : "ID") );

        studentProfileList.setAdapter(adapter);

        studentProfileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the data associated with the clicked item
                StudentProfile clickedProfile = studentProfiles.get(position);

                // Create an Intent to start the ProfileActivity
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

                // pass extra data
                intent.putExtra("surname", clickedProfile.getSurname());
                intent.putExtra("name", clickedProfile.getName());
                intent.putExtra("profileId", clickedProfile.getProfileID());
                intent.putExtra("gpa", clickedProfile.getGPA());
                intent.putExtra("dateCreated", clickedProfile.getProfileCreationDate().toString());

                // create an Access entry
                createAccessOpened(clickedProfile.getProfileID(), AccessType.OPENED, LocalDateTime.now());

                // Start the ProfileActivity
                startActivity(intent);
            }
        });
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

    // toggle Display mode
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toggle_profiles_display_mode && toggle.getTitle() == "By ID") {
            profileNameDisplayMode = false;
            onStart();

            return false;
        } else if (id == R.id.toggle_profiles_display_mode && toggle.getTitle() == "By Surname"){
            profileNameDisplayMode = true;

            onStart();

            return false;
        }

        return super.onOptionsItemSelected(item);
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

    protected void createAccessOpened(long profileID, AccessType accessType, LocalDateTime timestamp){
        Access access = new Access(profileID, accessType, timestamp);
        accessDBHelper.insertAccess(access, getApplicationContext());
    }
}