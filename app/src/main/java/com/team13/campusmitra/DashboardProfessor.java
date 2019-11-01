package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

public class DashboardProfessor extends AppCompatActivity {


    ImageView image;


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
        loadImage();

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
}
