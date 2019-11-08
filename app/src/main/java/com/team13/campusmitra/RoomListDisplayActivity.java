package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.RoomListDisplayAdaptor;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.util.ArrayList;

public class RoomListDisplayActivity extends AppCompatActivity {

    private static final String TAG="RoomListDisplayActivity";
    private ArrayList<Room> room_items = new ArrayList<>();
    private ImageButton imageButton;
    private RoomListDisplayAdaptor roomAdaptor;
    //SearchView searchView;
    ImageButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list_display);
        //searchView=findViewById(R.id.rooms_list_search_view);
        addButton=findViewById(R.id.rooms_list_display_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Rooms.class);
                startActivity(intent);
            }
        });
        loadRoomData();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                filterData(s);
//                return false;
//            }
//        });


    }
    private void filterData(String s){
        ArrayList<Room> updatedList = new ArrayList<>();
        for(Room r:room_items){
            if(s!=null&&r!=null&&(s.toLowerCase().contains(r.getRoomNumber().toLowerCase())||s.toLowerCase().contains(r.getRoomBuilding().toLowerCase())))
            {
                updatedList.add(r);
            }
        }
        if (updatedList.size()>0)
        roomAdaptor.doSomething(updatedList);

    }


    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.rooms_list_display_recycler_view);
        roomAdaptor = new RoomListDisplayAdaptor(RoomListDisplayActivity.this,room_items);
        recyclerView.setAdapter(roomAdaptor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration did = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(did);
    }

    private void loadRoomData() {
        FirebaseRoomHelper helper = new FirebaseRoomHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                room_items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = snapshot.getValue(Room.class);
                    room_items.add(room);

                }
                //progressBar.setVisibility(View.GONE);
                if (room_items.size() > 0) {
                    Log.d("abcd","hdefh"+room_items);
                    Log.d("lololo", "onDataChange: " + room_items.get(1).getRoomNumber());
                    initRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    public void sendToLab(View view)
    {
        Intent intent=new Intent(getApplicationContext(),LabsRecyclerView.class);
        startActivity(intent);
    }
}
