package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.team13.campusmitra.dataholder.EmailHolder;

public class FirebaseApprovFacultyHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseApprovFacultyHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("approvefaculty");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void addToApproveList(Context context, EmailHolder holder){
        String uid = firebaseDatabase.push().getKey();
        holder.setEmailID(uid);
        firebaseDatabase.child(uid).setValue(holder);
        Toast.makeText(context,"Faculty email added successfully!!",Toast.LENGTH_SHORT).show();
    }
    public void updateEmail(Context context, EmailHolder holder){
        String uid = holder.getEmailID();
        firebaseDatabase.child(uid).setValue(holder);
        Toast.makeText(context,"Email updated successfully!!",Toast.LENGTH_SHORT).show();
    }

}
