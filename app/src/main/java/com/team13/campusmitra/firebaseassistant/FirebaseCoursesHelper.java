package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Course;

public class FirebaseCoursesHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseCoursesHelper(){
       firebaseDatabase = FirebaseDatabase.getInstance().getReference("courses");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void addCourse(Context context, Course course){
        String uid = firebaseDatabase.push().getKey();
        course.setCourseID(uid);
        firebaseDatabase.child(uid).setValue(course);
        Toast.makeText(context,"Course added successfully!!",Toast.LENGTH_SHORT).show();
    }
    public void updateCourse(Context context, Course course){
        String uid = course.getCourseID();
        course.setCourseID(uid);
        firebaseDatabase.child(uid).setValue(course);
        Toast.makeText(context,"Course updated successfully!!",Toast.LENGTH_SHORT).show();
    }

}
