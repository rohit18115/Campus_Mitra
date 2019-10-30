package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpenDialogue extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_dialogue);

        textView = findViewById(R.id.textViewtowrite);
        button = findViewById(R.id.buttontoclick);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                UpdateProfessorDialogue profDialogue = new UpdateProfessorDialogue(2);
//                profDialogue.show(getSupportFragmentManager(),"i dont know");
                BookingDialogue bookingDialogue = new BookingDialogue(BookingDialogue.APPOINTMENT);
                bookingDialogue.setAppointmentDetails("sdadf","sfsf");
                bookingDialogue.show(getSupportFragmentManager(),"Dialog Box Here!");
            }
        });

    }
}
