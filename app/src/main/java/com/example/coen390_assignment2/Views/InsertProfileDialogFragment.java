package com.example.coen390_assignment2.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.coen390_assignment2.Controllers.StudentProfileDBHelper;
import com.example.coen390_assignment2.Models.StudentProfile;
import com.example.coen390_assignment2.R;

import java.time.LocalDate;

public class InsertProfileDialogFragment extends DialogFragment {

    private EditText profile_surname_edit_text, profile_name_edit_text, profile_ID_edit_text, profile_GPA_edit_text;

    private Button cancelButton, saveButton;

    public InsertProfileDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert_profile_dialog, container, false);

        profile_surname_edit_text = view.findViewById(R.id.profile_surname_edit_text);
        profile_name_edit_text = view.findViewById(R.id.profile_name_edit_text);
        profile_ID_edit_text = view.findViewById(R.id.profile_ID_edit_text);
        profile_GPA_edit_text = view.findViewById(R.id.profile_GPA_edit_text);

        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String surname = profile_surname_edit_text.getText().toString();
                String name = profile_name_edit_text.getText().toString();

                String ID = profile_ID_edit_text.getText().toString();
                String GPA = profile_GPA_edit_text.getText().toString();

                try {
                    float gpa = Float.parseFloat(GPA);
                    if (
                            !(surname.isEmpty() ||
                                    name.isEmpty() ||
                                    ID.isEmpty() ||
                                    GPA.isEmpty() ||
                                    ID.length() != 8 ||
                                    (gpa <= 0.0f || gpa >= 4.3f))) {

                        long id = Long.parseLong(ID);

                        StudentProfile profile = new StudentProfile(surname, name, id, gpa, LocalDate.now());
                        StudentProfileDBHelper dbHelper = new StudentProfileDBHelper(getActivity());
                        dbHelper.insertStudentProfile(profile, getContext());

                        ((MainActivity) getActivity()).onStart();

                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Invalid input!", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid input!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
