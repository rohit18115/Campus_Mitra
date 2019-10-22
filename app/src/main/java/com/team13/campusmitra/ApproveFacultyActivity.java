package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.EmailHolder;
import com.team13.campusmitra.firebaseassistant.FirebaseApprovFacultyHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ApproveFacultyActivity extends AppCompatActivity {

    ImageView addButton;
    ListView mylist;
    SearchView searchView;
    ArrayList<EmailHolder> emailHolders;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_faculty);
        initComponents();
        loadData();
    }
    private void initComponents(){
        addButton = findViewById(R.id.approve_add_btn);
        mylist = findViewById(R.id.approve_listview);
        searchView=findViewById(R.id.approve_searchview);
        emailHolders = new ArrayList<>();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addDetails();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }
    private void fitData(){
        ArrayList<String> ar = new ArrayList<>();
        for(EmailHolder holder:emailHolders){
            ar.add(holder.getEmail());
        }
        Object[] objects = ar.toArray();
        String []items = Arrays.copyOf(objects,objects.length, String[].class);
        adapter = new ArrayAdapter<>(this,R.layout.emailholder_list_element,R.id.ap_list_ele_tv,items);
        mylist.setAdapter(adapter);

    }
    private void loadData(){
        DatabaseReference reference = new FirebaseApprovFacultyHelper().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailHolders.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    EmailHolder holder = snapshot.getValue(EmailHolder.class);
                    emailHolders.add(holder);
                }
                fitData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addDetails(){
        final AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_approv_faculty, null);
        final EditText email  = view.findViewById(R.id.dialog_add_approv_email);
        final EditText pass = view.findViewById(R.id.dialog_add_approv_pass);
        Button button = view.findViewById(R.id.dialog_add_approv_btn);


        alertDialog.setView(view);
        alertDialog.setTitle("Add Faculty Email Record");
        dialog= alertDialog.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString().trim();
                String pas = pass.getText().toString().trim();
                if(em.length()<1){
                    email.setError("Enter Email");
                }
                else if(!em.contains("@iiitd.ac.in")){
                    email.setError("Enter valid iiitd email");
                }
                else if(pas.length()<1){
                    pass.setError("Enter password");
                }
                else{
                    FirebaseApprovFacultyHelper helper = new FirebaseApprovFacultyHelper();
                    EmailHolder holder = new EmailHolder();
                    holder.setEmail(em);
                    holder.setPassword(pas);
                    helper.addToApproveList(getApplicationContext(),holder);
                    dialog.cancel();
                }

            }
        });
    }
}
