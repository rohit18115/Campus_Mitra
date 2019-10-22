package com.team13.campusmitra;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team13.campusmitra.adaptors.ProfListDisplayAdaptor;
import com.team13.campusmitra.adaptors.StudentListDisplayAdaptor;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;

import java.util.ArrayList;

public class UserListDisplayActivity extends AppCompatActivity {

    private static final String TAG = "UserListDisplayActivity";

    private ArrayList<User> items = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Faculty> profs = new ArrayList<>();
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_display);

        Log.d(TAG, "onCreate: started");
        if(type == 0)
            initStudentComponents();
        else
            initProfComponent();
        initRecycler();
    }

    private void initProfComponent() {
        Log.d(TAG, "initImage: started");
        String url = "https://drive.google.com/uc?export=download&id=1y72ODb4maSRFbO-rjuJTVIEJC20LUmti";
        User user = new User();
        user.setUserFirstName("Rohit");
        user.setUserLastName("Arora");
        user.setUserEmail("rohit18115@iiitd.ac.in");
        Faculty prof = new Faculty();
        for (int i = 0; i < 15; i++) {
            items.add(user);
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
        Student student = new Student();
        for (int i = 0; i < 15; i++) {
            items.add(user);
            students.add(student);
        }
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.user_display_recycler_view);
        if(type == 0) {
            StudentListDisplayAdaptor adaptor = new StudentListDisplayAdaptor(items,students,this);
            recyclerView.setAdapter(adaptor);
        }
        else {
            ProfListDisplayAdaptor adaptor = new ProfListDisplayAdaptor(items,profs,this);
            recyclerView.setAdapter(adaptor);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration did = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(did);
    }
}
