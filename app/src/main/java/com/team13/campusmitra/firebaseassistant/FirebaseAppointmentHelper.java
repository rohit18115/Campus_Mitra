package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Appointment;

public class FirebaseAppointmentHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseAppointmentHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("appointments");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void  addAppointment(Context context, Appointment appointment){
        String uid = firebaseDatabase.push().getKey();
        appointment.setAppointmentID(uid);
        firebaseDatabase.child(uid).setValue(appointment);
        Toast.makeText(context,"Appointment added successfully",Toast.LENGTH_LONG);
    }
    public void updateAppointment(Context context,Appointment appointment){
        String uid = appointment.getAppointmentID();
        firebaseDatabase.child(uid).setValue(appointment);

        Toast.makeText(context,"Appointment updated successfully",Toast.LENGTH_LONG);
    }
}
