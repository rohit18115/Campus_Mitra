package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardProfessor extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_professor);
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
}
