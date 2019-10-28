package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Project;

public class FirebaseProjectHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseProjectHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("project");
    }
    
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void addProject(Context context, Project project){
        String uid = firebaseDatabase.push().getKey();
        project.setProjectID(uid);
        firebaseDatabase.child(uid).setValue(project);
        Toast.makeText(context,"Course added successfully!!",Toast.LENGTH_SHORT).show();
    }
    public void updateProject(Context context, Project project){
        String uid = project.getProjectID();
        project.setProjectID(uid);
        firebaseDatabase.child(uid).setValue(project);
        Toast.makeText(context,"Course updated successfully!!",Toast.LENGTH_SHORT).show();
    }
}
