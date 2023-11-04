package com.example.coen390_assignment2.Views;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coen390_assignment2.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Button showDialogButton = findViewById(R.id.add_profile_action_button);

        // Initialize the ActionBar (Toolbar)
        setSupportActionBar(toolbar);

        // TODO replace with actual database info
        toolbar.setSubtitle("New Subtitle Text !!!!!!!!!!!!!!");

        initAddProfileActionButton(showDialogButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_main_activity, menu);
        return true;
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