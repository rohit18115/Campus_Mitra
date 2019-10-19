package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseFacultyHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseFacultyHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("faculty");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
