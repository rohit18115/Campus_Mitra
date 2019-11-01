package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.StudentProfile;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;

public class FirebaseStudentHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseStudentHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("student");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }

    public void addStudent(Context context, Student student) {
        String uid = student.getUserID();
        firebaseDatabase.child(uid).setValue(student);
        Toast.makeText(context,"Student added successfully!!",Toast.LENGTH_SHORT).show();
    }
}
