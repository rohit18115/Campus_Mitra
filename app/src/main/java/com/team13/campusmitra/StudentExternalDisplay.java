package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.widget.ProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentExternalDisplay extends AppCompatActivity {

    CircleImageView image;
    AppCompatTextView name;
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
        setContentView(R.layout.activity_student_external_display);

        initComponent();
    }
}
