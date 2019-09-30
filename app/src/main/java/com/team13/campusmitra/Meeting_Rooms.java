package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Meeting_Rooms extends AppCompatActivity {
    TextView textView_M;
    Button button_M;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__rooms);
        textView_M=findViewById(R.id.meeting_room_capacity);
        button_M=findViewById(R.id.button_discussion_done);
    }
}
