package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    ProgressBar pb;

    protected void initComponent() {
        image = findViewById(R.id.ase_profile_image);
        name = findViewById(R.id.ase_profile_name);
        dept = findViewById(R.id.ase_department);
        email = findViewById(R.id.ase_email_id);
        courses = findViewById(R.id.ase_enrolled_courses);
        interests = findViewById(R.id.ase_interests);
        pb = findViewById(R.id.ase_pb);
    }

    protected void loadData() {



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_display);
        fab = findViewById(R.id.fab);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Click on each component to edit.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                name = findViewById(R.id.ase_profile_name);
                name.setOnClickListener(this);

                animateFab();
            }
    });
    }
        private void animateFab(){

            fab.startAnimation(rotateForward);
            fab.startAnimation(rotateBackward);

        }


    @Override
    public void onClick(View v) {

    }
}
