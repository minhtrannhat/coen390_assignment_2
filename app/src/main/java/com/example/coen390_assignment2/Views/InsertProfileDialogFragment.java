package com.example.coen390_assignment2.Views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.coen390_assignment2.R;

public class InsertProfileDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.
        builder.setView(inflater.inflate(R.layout.dialog_profile_create, null))
                // Add action buttons
                .setPositiveButton(R.string.save_text_dialog_fragment, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Sign in the user.
                    }
                })
                .setNegativeButton(R.string.cancel_text_dialog_fragment, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InsertProfileDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
