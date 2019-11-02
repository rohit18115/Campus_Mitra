package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;
public class DashboardProfessor extends AppCompatActivity {
    CardView rl;

    ImageView image;
    TextView nameTV;
    User user;


    protected void loadImage() {

        image = findViewById(R.id.imgViewProf);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getUserId().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + user.getUserLastName());
                        String gender = user.getGender();
                        if(gender.equals("Male")) {
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(user.getImageUrl())
                                    .placeholder(R.drawable.blankboy)
                                    .into(image);
                        }
                        else{
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(user.getImageUrl())
                                    .placeholder(R.drawable.blankgirl)
                                    .into(image);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_professor);
        rl = findViewById(R.id.proff_views_rl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardProfessor.this, GotoAddEditRL.class);
                startActivity(intent);
            }
        });

        loadImage();
        nameTV = findViewById(R.id.prof_name);
        user  = (User) getIntent().getSerializableExtra("MYKEY");
        System.out.println("abcd"+user);
        nameTV.setText("Hello "+user.getUserFirstName()+" "+user.getUserLastName());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.scan_id:
                Intent intent = new Intent(getApplicationContext(), OCRActivity.class);

                startActivity(intent);
                break;
            case R.id.view_profile:
                Intent intent2 = new Intent(getApplicationContext(), FacultyProfileDisplay.class);

                startActivity(intent2);
                return true;
            case R.id.add_projects:
                Intent intent3 = new Intent(getApplicationContext(), Add_Project.class);

                startActivity(intent3);
                return true;
            case R.id.logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent1 = new Intent(getApplicationContext(),SignInSplash.class);
                startActivity(intent1);
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void send_to_recyc(View view)
    {
        Intent intent = new Intent(getApplicationContext(), UserListDisplayActivity.class);
        intent.putExtra("userType",0);
        startActivity(intent);
        //finish();
    }

    public void send_to_recyc_pro(View view)
    {
        Intent intent = new Intent(getApplicationContext(), UserListDisplayActivity.class);
        intent.putExtra("userType",1);
        startActivity(intent);
        //finish();
    }
    public void loadVacantActivityProf(View view){
        Intent intent = new Intent(getApplicationContext(),VacantRoomDetails.class);
        intent.putExtra("userType",1);
        startActivity(intent);

    }
}
