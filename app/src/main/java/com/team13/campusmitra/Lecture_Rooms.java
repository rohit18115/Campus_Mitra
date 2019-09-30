package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Lecture_Rooms extends AppCompatActivity {
    TextView textView_L;
    Button button_L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture__rooms);
        textView_L=findViewById(R.id.lecture_room_capacity);
        button_L=findViewById(R.id.button_lecture_done);
    }
}
