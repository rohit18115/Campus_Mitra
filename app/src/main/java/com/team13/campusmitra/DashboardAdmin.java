package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DashboardAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
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
            return true;
        case R.id.logout:
            Intent i = new Intent(getApplicationContext(), SignInSplash.class);

            startActivity(i);
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
    public void sendToTimetable(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AddTimeTableActivity.class);

        startActivity(intent);

    }
    public void sendToCourse(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);

        startActivity(intent);

    }
    public void sentToAddRoom(View view){
        Intent intent = new Intent(getApplicationContext(),Rooms.class);
        startActivity(intent);
    }
    public void sendToApproveFaculty(View view){
        Intent intent = new Intent(getApplicationContext(),ApproveFacultyActivity.class);
        startActivity(intent);
    }

}
