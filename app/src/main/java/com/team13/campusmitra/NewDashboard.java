package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.team13.campusmitra.dataholder.User;

public class NewDashboard extends AppCompatActivity {
    TextView nameTV;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);
        nameTV = findViewById(R.id.student_dashboard_name);
        user  = (User) getIntent().getSerializableExtra("MYKEY");
        nameTV.setText("Hello "+user.getUserFirstName()+" "+user.getUserLastName());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    public void sentToAddRoom(View view) {
    }
}
