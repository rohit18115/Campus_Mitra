package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCoursesHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseCoursesHelper(){
       firebaseDatabase = FirebaseDatabase.getInstance().getReference("courses");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }

}
