package com.team13.campusmitra;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public class StudentProfile extends AppCompatActivity implements View.OnClickListener {
    TextView display_courses;
    Button select_courses;
    static final int PICK_CONTACT_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        select_courses = findViewById(R.id.selectcourses);
        display_courses = (TextView)findViewById(R.id.display_courses_id);
        select_courses.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.selectcourses :
                Intent intent1 = new Intent(StudentProfile.this, SelectCoursesRecyclerView.class);
                startActivityForResult(intent1, PICK_CONTACT_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String t = "";
                if(data!=null)
                     t = data.getStringExtra("selected_course_Name");
                if(display_courses != null)
                    display_courses.setText(t);
            }
        }
    }

}

