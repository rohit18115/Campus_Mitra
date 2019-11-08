package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.EmailHolder;
import com.team13.campusmitra.firebaseassistant.FirebaseApprovFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ApproveFacultyActivity extends AppCompatActivity {

    ImageView addButton;
    ListView mylist;
    SearchView searchView;
    ArrayList<EmailHolder> emailHolders;
    ArrayAdapter<String> adapter;
    String []items;
    private TextInputEditText text;
    private ArrayList<Object> emailid;

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
        emailid = new ArrayList<>();
        for(EmailHolder holder:emailHolders){
            ar.add(holder.getEmail());
            emailid.add(holder.getEmailID());
        }
        Object[] objects = ar.toArray();
        items = Arrays.copyOf(objects,objects.length, String[].class);
        adapter = new ArrayAdapter<>(this,R.layout.emailholder_list_element,R.id.ap_list_ele_tv,items);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                android.app.AlertDialog.Builder builderDomain = new android.app.AlertDialog.Builder(ApproveFacultyActivity.this);
                builderDomain.setTitle("Delete Email");
                builderDomain.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        final String txt = text.getText().toString();
//                        if(txt!=null && !txt.isEmpty()) {
//                            FirebaseApprovFacultyHelper helper = new FirebaseApprovFacultyHelper();
//                            Log.d("tagemail", "onClick: "+pos+ " "+items[pos]);
//                            DatabaseReference ref = helper.getReference().child(emailid.get(pos).toString());
//                            ref.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    EmailHolder holder = dataSnapshot.getValue(EmailHolder.class);
//                                    if(holder!=null)
//                                        holder.setEmail(txt);
//                                    new FirebaseApprovFacultyHelper().updateEmail(getApplicationContext(),holder);
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                            //ref.removeValue();
//                        } else {
//                            Toast.makeText(ApproveFacultyActivity.this,"Email cant be empty",Toast.LENGTH_SHORT).show();
//                        }
                    }
                });

                builderDomain.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseApprovFacultyHelper helper = new FirebaseApprovFacultyHelper();
                        DatabaseReference ref = helper.getReference().child(emailid.get(pos).toString());
                        ref.removeValue();
                        dialog.cancel();
                    }
                });
                builderDomain.show();
            }
        });

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
        Button button = view.findViewById(R.id.dialog_add_approv_btn);


        alertDialog.setView(view);
        alertDialog.setTitle("Add Faculty Email Record");
        dialog= alertDialog.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString().trim();
                if(em.length()<1){
                    email.setError("Enter Email");
                }
                else if(!em.contains("@iiitd.ac.in")){
                    email.setError("Enter valid iiitd email");
                }
                else{
                    FirebaseApprovFacultyHelper helper = new FirebaseApprovFacultyHelper();
                    EmailHolder holder = new EmailHolder();
                    holder.setEmail(em);
                    helper.addToApproveList(getApplicationContext(),holder);
                    dialog.cancel();
                }

            }
        });
    }
}
