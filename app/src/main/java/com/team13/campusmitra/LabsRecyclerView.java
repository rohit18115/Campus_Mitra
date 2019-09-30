package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class LabsRecyclerView extends AppCompatActivity {

    private static final String TAG = "LabsRecyclerView";

    private ArrayList<String> labNumber = new ArrayList<>();
    private ArrayList<String> count = new ArrayList<>();
    private ArrayList<String> address = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labs_recycler_view);
        Log.d(TAG, "onCreate: started");

        initComponents();
        initRecycler();
    }

    private void initComponents() {

        Log.d(TAG, "initImage: started");
        String url = "https://drive.google.com/file/d/1y72ODb4maSRFbO-rjuJTVIEJC20LUmti/view?usp=sharing";
        String labNum = "A-403";
        String occupancy = "40";
        String addr = "Situated at RnD Block, 4th Floor, System Count: 10";
        for (int i = 0; i < 15; i++) {
            imageUrls.add(url);
            labNumber.add(labNum);
            count.add(occupancy);
            address.add(addr);
        }
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.labs_recycler_view);
        LabsRecyclerViewAdaptor adapter = new LabsRecyclerViewAdaptor(labNumber,count, address,imageUrls, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
