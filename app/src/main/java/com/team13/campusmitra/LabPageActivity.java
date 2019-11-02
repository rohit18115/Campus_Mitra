package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team13.campusmitra.dataholder.Room;

public class LabPageActivity extends AppCompatActivity {
    Room room;
    ImageView imageView;
    TextView roomnumber;
    TextView roomBuilding;
    TextView systemcount;
    TextView roomType;
    TextView liveCount;
    TextView roomCapacity;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_page);
        initComponents();
    }
    private void initComponents(){
        intent=getIntent();
        room = (Room) intent.getSerializableExtra("ROOM");
        imageView = findViewById(R.id.activity_lab_imageview);
        roomnumber = findViewById(R.id.activity_lab_roomnumber);
        roomBuilding = findViewById(R.id.activity_lab_roombuilding);
        systemcount = findViewById(R.id.activity_lab_systemcount);
        roomType = findViewById(R.id.activity_lab_roomtype);
        liveCount = findViewById(R.id.activity_lab_livecount);
        Glide.with(this)
                .asBitmap()
                .load(room.getRoomImageURL())
                .placeholder(R.drawable.labs)
                .into(imageView);

    }

}
