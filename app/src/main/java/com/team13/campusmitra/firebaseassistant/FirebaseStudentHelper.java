package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseStudentHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseStudentHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("student");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
