package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseResearchLabHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseResearchLabHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("researchlab");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
