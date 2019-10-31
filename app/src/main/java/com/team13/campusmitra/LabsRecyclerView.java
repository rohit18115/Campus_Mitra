package com.team13.campusmitra;

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

import com.team13.campusmitra.adaptors.LabsRecyclerViewAdaptor;
import com.team13.campusmitra.dataholder.Room;

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
        adapter = new LabsRecyclerViewAdaptor(items, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration did = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(did);
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
