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
import android.widget.TextView;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewDashboard extends AppCompatActivity {
    TextView nameTV;
    User user;
    private CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);
        nameTV = findViewById(R.id.student_dashboard_name);
        user  = (User) getIntent().getSerializableExtra("MYKEY");

        nameTV.setText("Hello "+user.getUserFirstName()+" "+user.getUserLastName());

        CircleImageView imageView = findViewById(R.id.imgViewStudent);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), StudentProfileDisplay.class);

                startActivity(intent2);
                return true;
            }
        });
        loadImage();
    }

    protected void loadImage() {

        image = findViewById(R.id.imgViewStudent);

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.iiitd);// set drawable icon
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.student_actionbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.scan_id:
                Intent intent = new Intent(getApplicationContext(), OCRActivity.class);

                startActivity(intent);
                return true;
            case R.id.view_profile:
                Intent intent2 = new Intent(getApplicationContext(), StudentProfileDisplay.class);

                startActivity(intent2);
                return true;
            case R.id.timetable:
                String url = "https://www.iiitd.ac.in/sites/default/files/docs/admissions/2019/Time%20Table-Monsoon%202019V6.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.calendar:
                String url1 = "https://www.iiitd.ac.in/sites/default/files/docs/admissions/2019/Academic%20Calendar%20Monsoon%202019_Final.pdf";
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(url1));
                startActivity(i1);
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

    public void send_to_ResearchLabs(View view){
        Intent intent2 = new Intent(this, ResearchLabsRecyclerView.class);
        intent2.putExtra("UTYPE",""+user.getUserType());
        startActivity(intent2);
    }
    public void send_to_scan(View view)
    {
        Intent intent = new Intent(getApplicationContext(), OCRActivity.class);

        startActivity(intent);
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


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signOut();
//    }

    public void sendtoViewCourse(View view){
        Intent intent = new Intent(getApplicationContext(), viewcourse_student.class);
        //intent.putExtra("userType",1);
        startActivity(intent);
    }

    public void sentToAddRoom(View view) {
    }

    public void send_to_task(View view) {
        Intent intent = new Intent(getApplicationContext(), UserAppointment.class);
        //intent.putExtra("userId", user.getUserId());
        startActivity(intent);
    }
    public void send_to_recyc_lab(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ResearchLabsRecyclerView.class);
        intent.putExtra("userType",1);
        startActivity(intent);
        //finish();
    }
    public void send_to_stud_profile(View view)
    {
        Intent intent = new Intent(getApplicationContext(), StudentProfileDisplay.class);
        intent.putExtra("userType",1);
        startActivity(intent);
        //finish();
    }
}
