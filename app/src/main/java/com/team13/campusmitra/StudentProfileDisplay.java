package com.team13.campusmitra;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileDisplay extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    CircleImageView image;
    AppCompatTextView name;
    AppCompatTextView rollnumber;
    AppCompatTextView dept;
    AppCompatTextView email;
    AppCompatTextView courses;
    AppCompatTextView interests;
    TextInputEditText ETName;
    TextInputEditText ETRollNumber;
    TextInputEditText ETEmail;
    TextInputEditText ETInterests;
    TextInputEditText ETDepartment;
    ProgressBar pb;

    protected void initComponent() {
        image = findViewById(R.id.ase_profile_image);
        name = findViewById(R.id.ase_profile_name);
        rollnumber = findViewById(R.id.ase_rollnumber);
        dept = findViewById(R.id.ase_department);
        email = findViewById(R.id.ase_email_id);
        courses = findViewById(R.id.ase_enrolled_courses);
        interests = findViewById(R.id.ase_interests);
        pb = findViewById(R.id.ase_pb);
        fab = findViewById(R.id.fab);
    }

    protected void loadData() {



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_display);
        initComponent();
        setUpUIView();

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        fab.setOnClickListener(this);
        name.setOnClickListener(this);
        image.setOnClickListener(this);
        dept.setOnClickListener(this);
        email.setOnClickListener(this);
        courses.setOnClickListener(this);
        interests.setOnClickListener(this);
        rollnumber.setOnClickListener(this);
        name.setEnabled(false);
        image.setEnabled(false);
        dept.setEnabled(false);
        email.setEnabled(false);
        courses.setEnabled(false);
        interests.setEnabled(false);
        rollnumber.setEnabled(false);


    }
    private void animateFab(){
        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);
    }
    private void setUpUIView(){
        ETName = new TextInputEditText(this);
        ETEmail = new TextInputEditText(this);
        ETEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ETRollNumber = new TextInputEditText(this);
        ETInterests = new TextInputEditText(this);
        ETDepartment = new TextInputEditText(this);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab:
                animateFab();
                fab.setImageResource(R.drawable.ic_check);
                Snackbar.make(v, "Click on each component to edit.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                name.setEnabled(true);
                image.setEnabled(true);
                dept.setEnabled(true);
                email.setEnabled(true);
                courses.setEnabled(true);
                interests.setEnabled(true);
                rollnumber.setEnabled(true);




                break;
            case R.id.ase_profile_name:
                name.setEnabled(true);
                image.setEnabled(true);
                dept.setEnabled(true);
                email.setEnabled(true);
                courses.setEnabled(true);
                interests.setEnabled(true);
                rollnumber.setEnabled(true);


                AlertDialog.Builder builderName = new AlertDialog.Builder(this);
                builderName.setTitle("Name");
                builderName.setView(ETName);
                builderName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(ETName.getEditableText().toString());
                        Toast.makeText(StudentProfileDisplay.this,"Name has been successfully changed",Toast.LENGTH_LONG).show();
                    }
                });

                builderName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETName.getParent() != null) {
                    ((ViewGroup)ETName.getParent()).removeView(ETName); // <- fix
                }
                builderName.show();

                break;
            case R.id.ase_interests:
                name.setEnabled(true);
                image.setEnabled(true);
                dept.setEnabled(true);
                email.setEnabled(true);
                courses.setEnabled(true);
                interests.setEnabled(true);
                rollnumber.setEnabled(true);


                AlertDialog.Builder builderInterests = new AlertDialog.Builder(this);
                builderInterests.setTitle("Interests");
                builderInterests.setView(ETInterests);
                builderInterests.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        interests.setText(ETInterests.getEditableText().toString());
                        Toast.makeText(StudentProfileDisplay.this,"Interests have been successfully changed",Toast.LENGTH_LONG).show();
                    }
                });

                builderInterests.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETInterests.getParent() != null) {
                    ((ViewGroup)ETInterests.getParent()).removeView(ETInterests); // <- fix
                }
                builderInterests.show();
            break;
            case R.id.ase_email_id:
                name.setEnabled(true);
                image.setEnabled(true);
                dept.setEnabled(true);
                email.setEnabled(true);
                courses.setEnabled(true);
                interests.setEnabled(true);
                rollnumber.setEnabled(true);


                AlertDialog.Builder builderEmail = new AlertDialog.Builder(this);
                builderEmail.setTitle("Email");
                builderEmail.setView(ETEmail);
                builderEmail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        email.setText(ETEmail.getEditableText().toString());
                        Toast.makeText(StudentProfileDisplay.this,"Email has been successfully changed",Toast.LENGTH_LONG).show();
                    }
                });

                builderEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETEmail.getParent() != null) {
                    ((ViewGroup)ETEmail.getParent()).removeView(ETEmail); // <- fix
                }
                builderEmail.show();
                break;
            case R.id.ase_rollnumber:
                name.setEnabled(true);
                image.setEnabled(true);
                dept.setEnabled(true);
                email.setEnabled(true);
                courses.setEnabled(true);
                interests.setEnabled(true);
                rollnumber.setEnabled(true);


                AlertDialog.Builder builderRollNumber = new AlertDialog.Builder(this);
                builderRollNumber.setTitle("Roll Number");
                builderRollNumber.setView(ETRollNumber);
                builderRollNumber.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rollnumber.setText(ETRollNumber.getEditableText().toString());
                        Toast.makeText(StudentProfileDisplay.this,"Roll number has been successfully changed",Toast.LENGTH_LONG).show();
                    }
                });

                builderRollNumber.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETRollNumber.getParent() != null) {
                    ((ViewGroup)ETRollNumber.getParent()).removeView(ETRollNumber); // <- fix
                }
                builderRollNumber.show();

        }

    }
}
