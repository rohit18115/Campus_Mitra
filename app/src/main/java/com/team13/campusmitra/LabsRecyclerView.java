package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.team13.campusmitra.adaptors.LabsRecyclerViewAdaptor;
import com.team13.campusmitra.dataholder.Room;

import java.util.ArrayList;

public class LabsRecyclerView extends AppCompatActivity {

    private static final String TAG = "LabsRecyclerView";

    private ArrayList<Room> items = new ArrayList<>();

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
        String url = "https://drive.google.com/uc?export=download&id=1y72ODb4maSRFbO-rjuJTVIEJC20LUmti";
        Room room = new Room("123","A-403","RnD Block",0,url,"",40,"","",10);
        for (int i = 0; i < 15; i++) {
            items.add(room);
        }
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.labs_recycler_view);
        LabsRecyclerViewAdaptor adapter = new LabsRecyclerViewAdaptor(items, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration did = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(did);
    }
}
