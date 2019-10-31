package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Appointment;
import com.team13.campusmitra.dataholder.Booking;

public class FirebaseBookingHelper {

    private DatabaseReference firebaseDatabase;

    public FirebaseBookingHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("bookings");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void  addBooking(Context context, Booking booking){
        String uid = firebaseDatabase.push().getKey();
        booking.setBookingID(uid);
        firebaseDatabase.child(uid).setValue(booking);
        Toast.makeText(context,"Booked successfully",Toast.LENGTH_LONG);
    }
    public void updateBooking(Context context,Booking booking){
        String uid = booking.getBookingID();
        firebaseDatabase.child(uid).setValue(booking);

        Toast.makeText(context,"Booking updated successfully",Toast.LENGTH_LONG);
    }

}
