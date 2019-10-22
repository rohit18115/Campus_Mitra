package com.team13.campusmitra;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.ProfListDisplayAdaptor;
import com.team13.campusmitra.adaptors.StudentListDisplayAdaptor;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

public class UserListDisplayActivity extends AppCompatActivity {

    private static final String TAG = "UserListDisplayActivity";

    private ArrayList<User> items = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Faculty> profs = new ArrayList<>();
    private int type = 0;
    private ProfListDisplayAdaptor profAdaptor;
    private StudentListDisplayAdaptor studentAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_display);

        Log.d(TAG, "onCreate: started");
        if(type == 0)
           loadStudentData();
        else
            loadProfData();
    }

    private void initProfComponent() {
        Log.d(TAG, "initImage: started");
        String url = "https://drive.google.com/uc?export=download&id=1y72ODb4maSRFbO-rjuJTVIEJC20LUmti";
        User user = new User();
        user.setUserFirstName("Rohit");
        user.setUserLastName("Arora");
        user.setUserEmail("rohit18115@iiitd.ac.in");
        User user1 = new User();
        user1.setUserFirstName("Himanshi");
        user1.setUserLastName("Singh");
        user1.setUserEmail("himanshi18073@iiitd.ac.in");
        Faculty prof = new Faculty();
        for (int i = 0; i < 15; i++) {
            items.add(user);
            profs.add(prof);
            items.add(user1);
            profs.add(prof);
        }
    }

    private void initStudentComponents() {

        Log.d(TAG, "initImage: started");
        String url = "https://drive.google.com/uc?export=download&id=1y72ODb4maSRFbO-rjuJTVIEJC20LUmti";
        User user = new User();
        user.setImageUrl(url);
        user.setUserFirstName("Himanshi");
        user.setUserLastName("Singh");
        user.setUserEmail("himanshi18073@iiitd.ac.in");
        User user1 = new User();
        user1.setUserFirstName("Rohit");
        user1.setUserLastName("Arora");
        user1.setUserEmail("rohit18115@iiitd.ac.in");
        Student student = new Student();
        for (int i = 0; i < 1; i++) {
            items.add(user);
            students.add(student);
//            items.add(user1);
//            students.add(student);
        }
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.user_display_recycler_view);
        if(type == 0) {
            studentAdaptor = new StudentListDisplayAdaptor(items,students,this);
            recyclerView.setAdapter(studentAdaptor);
        }
        else {
            profAdaptor = new ProfListDisplayAdaptor(items,profs,this);
            recyclerView.setAdapter(profAdaptor);
        }
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
                if(type==0) {
                    studentAdaptor.getFilter().filter(newText);
                    return false;
                }
                else{
                    profAdaptor.getFilter().filter(newText);
                    return false;
                }

            }
        });
        return true;
    }

    private void loadStudentData() {

        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getUserType()==0) {
                        items.add(user);
                        students.add(new Student());
                        Log.d("lololo", "onDataChange: " + user.getUserLastName());
                    }
                }
                //progressBar.setVisibility(View.GONE);
                if (items.size()>0) {
                    Log.d("lololo", "onDataChange: " + items.get(0).getUserLastName());
                    initRecycler();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void loadProfData() {

        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getUserType()==1)
                        items.add(user);
                }
                //progressBar.setVisibility(View.GONE);
                if (items.size()>0)
                    initRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}


