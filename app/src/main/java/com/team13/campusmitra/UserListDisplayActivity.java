package com.team13.campusmitra;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

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
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
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

        if (savedInstanceState == null) {
            Bundle id = getIntent().getExtras();
            if(id == null) {
                Toast toast = Toast.makeText(this, "Error: No UserType found", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                type = id.getInt("userType");
            }
        }

        if(type == 0)
           loadStudentData();
        else
            loadProfData();
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
                    if(user.getUserType()==0 && user.getProfileCompleteCount()==2) {
                        items.add(user);
                        FirebaseStudentHelper fh = new FirebaseStudentHelper();
                        fh.getReference().child(user.getUserId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Student f = dataSnapshot.getValue(Student.class);
                                if(f!=null) {
                                    students.add(f);
                                } else {
                                    students.add(new Student());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                //progressBar.setVisibility(View.GONE);
                if (items.size()>0) {
                    Log.d("lololo", "onDataChange: " + items.get(0).getUserLastName());
                    initRecycler();
                } else {
                    //Toast
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

//    private void loadProfData() {
//
//        FirebaseUserHelper helper = new FirebaseUserHelper();
//        DatabaseReference reference = helper.getReference();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                items.clear();
//                profs.clear();
//                for(final DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    User user = snapshot.getValue(User.class);
//                    if(user.getUserType()==1 && user.getProfileCompleteCount()==2) {
//                        items.add(user);
//                        Log.d("lolo", "user added ");
//                        FirebaseFacultyHelper fh = new FirebaseFacultyHelper();
//                        fh.getReference().child(user.getUserId().trim()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Faculty f = dataSnapshot.getValue(Faculty.class);
//                                Log.d("lolo", " not failed");
//                                if(f!=null) {
//                                    profs.add(f);
//                                    Log.d("lolo", "prof added ");
//                                } else {
//                                    Log.d("lolo", "prof null added ");
//                                    profs.add(new Faculty());
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.d("lolo", "failed");
//                            }
//                        });
//                    }
//                }
//                //progressBar.setVisibility(View.GONE);
//                if (items.size()>0)
//                    initRecycler();
//                else {
//                    //Toast
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void loadProfData() {

        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                profs.clear();
                for(final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getUserType()==1 && user.getProfileCompleteCount()==2) {
                        items.add(user);
                        final String uid = user.getUserId().trim();
                        Log.d("lolo", "user added " + user.getUserFirstName());
                        FirebaseFacultyHelper fh = new FirebaseFacultyHelper();
                        fh.getReference().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                    Faculty f = snapshot.getValue(Faculty.class);
                                    Log.d("lolo", " not failed");
                                    if(f.getUserID().trim().equals(uid)) {
                                        if (f != null) {
                                            profs.add(f);
                                            Log.d("lolo", "prof added ");
                                        } else {
                                            Log.d("lolo", "prof null added ");
                                            profs.add(new Faculty());
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("lolo", "failed");
                            }
                        });
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




}


