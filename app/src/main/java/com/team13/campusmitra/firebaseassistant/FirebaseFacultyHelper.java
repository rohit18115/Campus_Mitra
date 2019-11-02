package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.FacultyProfile;
import com.team13.campusmitra.dataholder.Faculty;

public class FirebaseFacultyHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseFacultyHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("faculty");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }

    public void addFaculty(Context context, Faculty faculty) {

        String uid = faculty.getUserID();
        firebaseDatabase.child(uid).setValue(faculty);
        Toast.makeText(context,"Faculty added successfully!!",Toast.LENGTH_SHORT).show();

    }


}
