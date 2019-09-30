package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Discussion_Rooms extends AppCompatActivity {
    TextView textView_D;
    Button button_D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion__rooms);
        textView_D=findViewById(R.id.discussion_room_capacity);
        button_D=findViewById(R.id.button_discussion_done);
    }
}
