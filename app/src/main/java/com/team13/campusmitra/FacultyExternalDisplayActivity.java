package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class FacultyExternalDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_external_display);
    }

    public void BookAppointment(View view) {
        BookingDialogue bookingDialogue = new BookingDialogue(BookingDialogue.APPOINTMENT);
        /*
        TODO
        set UserID1 and userID2
         */
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user1ID = auth.getCurrentUser().getUid();
        String user2ID=""; /**/
        bookingDialogue.setAppointmentDetails("UserID1","UserID2");
        bookingDialogue.show(getSupportFragmentManager(),"Dialog Box for Appointment Booking");
    }
}
