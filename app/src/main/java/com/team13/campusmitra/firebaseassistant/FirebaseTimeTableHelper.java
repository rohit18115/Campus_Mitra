package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseTimeTableHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseTimeTableHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("timetable");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
