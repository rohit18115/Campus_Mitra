package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class StudentProfile extends AppCompatActivity {
    TextView display_courses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Bundle courses = getIntent().getExtras();
        display_courses = (TextView)findViewById(R.id.display_courses);
        display_courses.setText(courses.getString("selected_course_code"));
    }
}
