package com.team13.campusmitra;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

public class StudentProfile extends AppCompatActivity implements View.OnClickListener {
    TextView display_courses;
    TextView department;
    TextView interests;
    TextView rollNo;
    Button select_courses;
    static final int PICK_CONTACT_REQUEST = 1;

    private void initComponents() {
        display_courses = findViewById(R.id.display_courses_id);
        department = findViewById(R.id.department);
        interests = findViewById(R.id.interests);
        rollNo = findViewById(R.id.rollNumber);
    }

    private void getStudentObject() {
        Student student = new Student();
        student.setAreaOfInterest(interests.getText().toString());
        student.setDepartment(department.getText().toString());
        student.setRollNumber(rollNo.getText().toString());
        student.setCourses(display_courses.getText().toString());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        student.setUserID(uid);
        FirebaseStudentHelper helper = new FirebaseStudentHelper();
        //user.setProfileCompleteCount(user.getProfileCompleteCount()+1);
        //helper.addStudent(this,user);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        select_courses = findViewById(R.id.selectcourses);
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

