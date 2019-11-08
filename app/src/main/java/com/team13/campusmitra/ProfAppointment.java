package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.ProfAppointmentListAdaptor;
import com.team13.campusmitra.dataholder.Appointment;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseAppointmentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProfAppointment extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<Appointment> appointmentslist = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_appointment);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.rv_prof_appointment);
        progressBar = findViewById(R.id.prof_appointment_progress);
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        final String currentDate = simpleDateFormat.format(calendar.getTime());

        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAppointmentHelper helper = new FirebaseAppointmentHelper();
        final DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentslist.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if(user.getUid().equals(appointment.getUserID1())||user.getUid().equals(appointment.getUserID2())) {
                        if(currentDate.compareTo(appointment.getDate())<0)
                        {
                            appointmentslist.add(appointment);
                        }
                    }
                }
                final ArrayList<User> users = new ArrayList<>();
                FirebaseUserHelper userHelper = new FirebaseUserHelper();
                DatabaseReference reference1 = userHelper.getReference();
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            User u = snapshot.getValue(User.class);
                            users.add(u);
                        }

                        for(User user: users){
                            Log.d("PROF", "onDataChange: "+user.getUserEmail());
                        }

                        ProfAppointmentListAdaptor profAppointmentListAdaptor = new ProfAppointmentListAdaptor(ProfAppointment.this, users, appointmentslist);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(profAppointmentListAdaptor);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        progressBar.setVisibility(View.GONE);



    }

}

