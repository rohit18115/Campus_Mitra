package com.team13.campusmitra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
/*
    :::Used by admin to update professor details
 */
public class UpdateProfessorDialogue extends DialogFragment {

    private EditText emailID;
    private EditText newEmailID;
    private String title;
    private int type;

    public UpdateProfessorDialogue(int type) {
        this.type = type;
    }

    private void initializer(View view) {
        emailID = view.findViewById(R.id.email_id_prof);
        newEmailID = view.findViewById(R.id.new_email_id_prof);

        if(type == 0) {
            title = "Add Professor";
            emailID.setHint("e-mail");
        }
        else if(type == 1) {
            title = "Update Professor";
            newEmailID.setVisibility(View.VISIBLE);
            emailID.setHint("e-mail");
            newEmailID.setHint("new e-mail");
        }
        else if(type == 2) {
            title = "Delete Professor";
            emailID.setHint("e-mail");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.update_professor_dialog,null);
        builder.setView(view);
        builder.setTitle(title);
        builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //::::Add Professor
                if(type == 0) {
                    Toast.makeText(getContext(), "Add Prof:" + emailID.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                //::::Update Professor
                else if(type == 1) {
                    Toast.makeText(getContext(), "Update Prof:" + emailID.getText().toString() + "to:" + newEmailID.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                //::::Delete Professor
                else if(type == 2) {
                    Toast.makeText(getContext(), "Delete Prof:" + emailID.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setTitle(title);
        initializer(view);
        return builder.create();
    }

}
