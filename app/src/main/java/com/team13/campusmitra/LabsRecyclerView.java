package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.LabsRecyclerViewAdaptor;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

public class LabsRecyclerView extends AppCompatActivity {
    LabsRecyclerViewAdaptor adapter;
    private static final String TAG = "LabsRecyclerView";

    private ArrayList<Room> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labs_recycler_view);
        Log.d(TAG, "onCreate: started");

        initComponents();
    }

    private void initComponents() {

        FirebaseRoomHelper helper = new FirebaseRoomHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    if(room.getRoomType()<6) {
                        items.add(room);
                    }
                }
                //progressBar.setVisibility(View.GONE);
                if (items.size()>0)
                    initRecycler();
                else {
                    //Toast
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.labs_recycler_view);
        adapter = new LabsRecyclerViewAdaptor(items, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        DividerItemDecoration did = new DividerItemDecoration(this,layoutManager.getOrientation());
//        recyclerView.addItemDecoration(did);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selectcourse, menu);

        MenuItem searchItem = menu.findItem(R.id.SCapp_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
