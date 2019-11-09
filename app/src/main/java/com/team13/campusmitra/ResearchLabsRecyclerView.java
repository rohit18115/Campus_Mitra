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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.ResearchLabsRecyclerViewAdaptor;
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseResearchLabHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;
//Intent intent = new Intent(getApplicationContext(),R_Lab.class);
//        intent.putExtra("ROOM",room);
//        intent.putExtra("RL",researchLab);
public class ResearchLabsRecyclerView extends AppCompatActivity  {
    ResearchLabsRecyclerViewAdaptor adapter;
    private static final String TAG = "LabsRecyclerView";

    private ArrayList<ResearchLab> items = new ArrayList<>();
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_labs_recycler_view);
        Log.d(TAG, "onCreate: started");
        Toast.makeText(getApplicationContext(),"Long Click Item to Delete",Toast.LENGTH_SHORT).show();
        uid = getIntent().getStringExtra("UTYPE");
        loadLabData();
    }

    private void loadLabData() {

        FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    ResearchLab rLab = snapshot.getValue(ResearchLab.class);
                    items.add(rLab);
                }
                //progressBar.setVisibility(View.GONE);
                if (items.size()>0) {
                    Log.d("lololo", "onDataChange: " + items.get(0).getRoomID());
                    initRecycler();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.research_labs_recycler_view);
        ResearchLabsRecyclerViewAdaptor adapter = new ResearchLabsRecyclerViewAdaptor(items, this,uid, this);
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

