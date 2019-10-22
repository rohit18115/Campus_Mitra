package com.team13.campusmitra;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public class StudentProfile extends AppCompatActivity implements View.OnClickListener {
    TextView display_courses;
    Button select_courses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        select_courses = findViewById(R.id.selectcourses);
        display_courses = (TextView)findViewById(R.id.display_courses);
        select_courses.setOnClickListener(this);
        if (savedInstanceState == null) {
            Bundle courses = getIntent().getExtras();
            if(courses == null) {
                display_courses= null;
            } else {
                display_courses.setText(courses.getString("selected_course_Name"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.selectcourses :
                Intent intent1 = new Intent(StudentProfile.this, SelectCoursesRecyclerView.class);
                startActivity(intent1);
        }


    }
}

